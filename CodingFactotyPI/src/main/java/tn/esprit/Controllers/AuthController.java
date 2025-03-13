package tn.esprit.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.Configuration.JwtUtil;
import tn.esprit.Configuration.OTPGenerator;
import tn.esprit.Repository.UserRepository;
import tn.esprit.Services.EmailService;
import tn.esprit.Services.UserDetailsServiceImpl;
import tn.esprit.entities.User;
import tn.esprit.entities.Gender;
import tn.esprit.entities.Role;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")  // Permet l'accès depuis Angular
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;  // Injection de UserDetailsServiceImpl

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private EmailService emailService;

    private final String uploadDir = "uploads/"; // Dossier où stocker les images

    @PostMapping(value = "/register", consumes = {"multipart/form-data"})
    public ResponseEntity<?> register(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("dateOfBirth") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateOfBirth,
            @RequestParam("gender") String gender,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "cv", required = false) MultipartFile cv,  // <-- Changer de String à MultipartFile
            @RequestParam(value = "speciality", required = false) String speciality,
            @RequestParam(value = "companyName", required = false) String companyName,
            @RequestParam(value = "level", required = false) String level,
            @RequestParam(value = "grade", required = false) String grade,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam("role") String role) {

        // 1. Validation des entrées
        if (firstName == null || firstName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "First name is required"));
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Last name is required"));
        }
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
        }
        if (password == null || password.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Password is required"));
        }
        if (dateOfBirth == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Date of birth is required"));
        }
        if (gender == null || gender.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Gender is required"));
        }
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Phone number is required"));
        }
        if (role == null || role.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Role is required"));
        }

        // 2. Vérification de l'unicité de l'email
        if (userRepository.findByEmail(email) != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
        }

        // 3. Hachage du mot de passe
        String hashedPassword = passwordEncoder.encode(password);

        // 4. Création de l'utilisateur
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setDateOfBirth(dateOfBirth);
        user.setGender(Gender.valueOf(gender));
        user.setPhoneNumber(phoneNumber);
        user.setSpeciality(speciality);
        user.setCompanyName(companyName);
        user.setLevel(level);
        user.setGrade(grade);
        user.setAddress(address);
        user.setRole(Role.valueOf(role));

        // 5. Gestion de l'image de profil
        if (image != null && !image.isEmpty()) {
            try {
                String imagePath = saveImage(image);
                user.setImage(imagePath);
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
            }
        }
        if (cv != null && !cv.isEmpty()) {
            try {
                String cvPath = saveCV(cv);
                user.setCv(cvPath);
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
            }
        }
        // 6. Enregistrement de l'utilisateur dans la base de données
        userRepository.save(user);

        // 7. Retour de la réponse
        return ResponseEntity.ok(Map.of("message", "User registered successfully", "userId", user.getIdUser()));
    }
    @PostMapping("/check-email")
    public ResponseEntity<?> checkEmailExists(@RequestBody Map<String, String> request) {
        String email = request.get("email"); // Extraire l'e-mail du corps de la requête
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
        }

        // Vérifier si l'e-mail existe déjà
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(Map.of("exists", true)); // L'e-mail existe déjà
        } else {
            return ResponseEntity.ok(Map.of("exists", false)); // L'e-mail n'existe pas
        }
    }
    @PutMapping("/update/{idUser}")
    public ResponseEntity<?> modifyUser(@PathVariable Long idUser,
                                        @RequestParam(value = "firstName", required = false) String firstName,
                                        @RequestParam(value = "lastName", required = false) String lastName,
                                        @RequestParam(value = "email", required = false) String email,
                                        @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
                                        @RequestParam(value = "address", required = false) String address,
                                        @RequestParam(value = "image", required = false) MultipartFile image) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (firstName != null) user.setFirstName(firstName);
        if (lastName != null) user.setLastName(lastName);
        if (email != null) user.setEmail(email);
        if (phoneNumber != null) user.setPhoneNumber(phoneNumber);
        if (address != null) user.setAddress(address);

        if (image != null && !image.isEmpty()) {
            String imagePath = saveImage(image);
            user.setImage(imagePath);
        }

        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Utilisateur mis à jour avec succès !"));
    }



    @PutMapping("/update/{idUser}/image")
    public ResponseEntity<?> updateUserImage(@PathVariable Long idUser, @RequestParam("image") MultipartFile image) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (image != null && !image.isEmpty()) {
            String imagePath = saveImage(image);
            user.setImage(imagePath);
            userRepository.save(user);
            return ResponseEntity.ok(Map.of("message", "Image mise à jour avec succès !", "imagePath", imagePath, "user", user));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Aucune image fournie"));
        }
    }


    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> request) {
        try {
            // Vérification si le token est présent dans la requête
            if (!request.containsKey("token")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Token manquant"));
            }

            String token = request.get("token");

            // Vérification du token Google
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList("297153804677-lou7tiqpeipefqo60c2cltqn400ki2st.apps.googleusercontent.com"))
                    .build();

            GoogleIdToken idToken;
            try {
                idToken = verifier.verify(token);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token invalide ou expiré"));
            }

            if (idToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Impossible de vérifier le token Google"));
            }

            // Extraction des données utilisateur depuis le payload
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");
            String pictureUrl = (String) payload.get("picture");

            // Vérifier si l'utilisateur existe dans la base de données
            User user = userRepository.findByEmail(email);
            if (user == null) {
                // L'utilisateur n'existe pas, on crée un nouvel utilisateur avec un rôle par défaut
                user = new User();
                user.setEmail(email);
                user.setFirstName(firstName);
                user.setLastName(lastName);

                // Téléchargement de l'image et sauvegarde dans le dossier /uploads
                String imageFileName = saveImageFromUrl(pictureUrl);  // Méthode pour télécharger et enregistrer l'image
                user.setImage(imageFileName);

                user.setRole(Role.STUDENT); // Par défaut, tous les nouveaux utilisateurs sont étudiants

                // Générer un mot de passe aléatoire
                String generatedPassword = generateRandomPassword();
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodedPassword = passwordEncoder.encode(generatedPassword);

                // Enregistrer le mot de passe encodé dans la base de données
                user.setPassword(encodedPassword);

                user = userRepository.save(user);

                // Vous pouvez envoyer le mot de passe généré à l'utilisateur par email si nécessaire
            } else {
                // Si l'utilisateur existe et que l'image est nulle, on met à jour l'image
                if (user.getImage() == null) {
                    String imageFileName = saveImageFromUrl(pictureUrl);  // Téléchargement et sauvegarde de l'image
                    user.setImage(imageFileName);
                    userRepository.save(user);
                }
            }

            // Vérification que l'email et le mot de passe ne sont pas nulls ou vides
            if (user.getEmail() == null || user.getEmail().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "L'email est manquant"));
            }
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Le mot de passe est manquant"));
            }
            if (user.getRole() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Le rôle est manquant"));
            }

            // Créer un UserDetails avec le mot de passe généré pour la session
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    user.getEmail(), // Nom d'utilisateur (email)
                    user.getPassword(), // Mot de passe encodé
                    AuthorityUtils.createAuthorityList(user.getRole().name()) // Liste des rôles
            );

            // Générer un JWT pour l'utilisateur
            String jwtToken = jwtUtil.generateToken(userDetails);

            // Authentifier l'utilisateur dans le contexte de sécurité
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Retourner le JWT et les informations utilisateur
            return ResponseEntity.ok(Map.of(
                    "token", jwtToken,
                    "email", email,
                    "firstName", firstName,
                    "lastName", lastName,
                    "image", user.getImage(), // Utiliser le nom du fichier de l'image stockée
                    "role", user.getRole().name()
            ));
        } catch (Exception e) {
            e.printStackTrace(); // Afficher l'erreur complète pour le débogage
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur lors de la connexion Google", "details", e.getMessage()));
        }
    }

    // Méthode pour télécharger et sauvegarder l'image depuis une URL
    private String saveImageFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            InputStream inputStream = url.openStream();

            // Créer un nom unique pour l'image
            String fileName = UUID.randomUUID().toString() + ".jpg"; // Changez l'extension si nécessaire
            Path destinationPath = Paths.get(uploadDir).resolve(fileName);

            // Créer le dossier si nécessaire
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            inputStream.close();

            log.info("Image téléchargée et enregistrée avec succès : {}", destinationPath);

            return fileName; // Retourner le nom du fichier pour l'enregistrer en BD
        } catch (IOException e) {
            log.error("Erreur lors du téléchargement de l'image", e);
            throw new RuntimeException("Erreur lors du téléchargement de l'image", e);
        }
    }



    // Méthode pour générer un mot de passe aléatoire
    private String generateRandomPassword() {
        int length = 12; // Longueur du mot de passe
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            // Vérifier si l'utilisateur existe
            User userEntity = userRepository.findByEmail(user.getEmail());
            if (userEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Utilisateur non trouvé"));
            }

            // Vérifier si l'utilisateur est banni avant de procéder à l'authentification
            // In your controller where login is handled
            if (userDetailsServiceImpl.isUserBanned(userEntity)) {
                Timestamp banEndDate = userDetailsServiceImpl.getBanEndDate(userEntity);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "Your account is banned until " + banEndDate));
            }


            // Authentification de l'utilisateur si non banni
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );

            // Générer un OTP
            String otp = OTPGenerator.generateOTP();

            // Enregistrer l'OTP dans la base de données
            userEntity.setOtp(otp);
            userRepository.save(userEntity);

            // Envoyer l'OTP par e-mail
            emailService.sendOTPEmail(user.getEmail(), otp);

            // Retourner une réponse indiquant que l'OTP a été envoyé
            return ResponseEntity.ok(Map.of("message", "OTP envoyé à votre adresse e-mail", "email", user.getEmail()));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Email ou mot de passe incorrect"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Une erreur interne s'est produite"));
        }
    }


    private String saveImage(MultipartFile image) {
        // Créer le dossier s'il n'existe pas
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Vérifier si l'image est bien un fichier JPG ou PNG
        String contentType = image.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            throw new RuntimeException("Format d'image non supporté. Utilisez JPG ou PNG.");
        }

        // Générer un nom unique pour éviter les doublons
        String fileExtension = contentType.equals("image/png") ? ".png" : ".jpg";
        String fileName = UUID.randomUUID() + fileExtension;
        String imagePath = uploadDir + fileName;

        try {
            Path destinationPath = Paths.get(uploadDir).resolve(fileName);
            Files.copy(image.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("Image enregistrée avec succès : {}", destinationPath.toString());
        } catch (IOException e) {
            log.error("Erreur lors de l'enregistrement de l'image", e);
            throw new RuntimeException("Erreur lors de l'enregistrement de l'image.", e);
        }

        return fileName; // Retourne le nom du fichier pour l'enregistrer en BD
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = filename.endsWith(".png") ? "image/png" : "image/jpeg";

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalide ou manquant.");
        }

        // Supprime "Bearer " pour obtenir le token brut
        String jwt = token.substring(7);

        // Extraire l'email à partir du JWT
        String email = jwtUtil.extractEmail(jwt);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalide.");
        }

        // Trouver l'utilisateur en BD
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé.");
        }

        return ResponseEntity.ok(user);
    }

    @PutMapping("/accept/{idUser}")
    public ResponseEntity<?> acceptInstructor(@PathVariable Long idUser) {
        try {
            logger.info("Attempting to accept instructor with ID: " + idUser);
            Optional<User> optionalUser = userRepository.findById(idUser);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                if (Boolean.TRUE.equals(user.getAccepted())) {
                    logger.warn("User " + user.getFirstName() + " " + user.getLastName() + " is already accepted.");
                    // Réponse structurée
                    return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "User is already accepted."));
                }

                // Générer un mot de passe temporaire aléatoire
                String temporaryPassword = UUID.randomUUID().toString().substring(0, 8);

                // Hacher le mot de passe avant de le sauvegarder
                user.setPassword(passwordEncoder.encode(temporaryPassword));
                user.setAccepted(true);
                userRepository.save(user);

                logger.info("User " + user.getFirstName() + " accepted successfully.");

                if (user.getEmail() != null) {
                    try {
                        // Envoyer l'email avec le mot de passe temporaire
                        emailService.sendAcceptedEmail(user.getEmail(), user.getFirstName(), temporaryPassword);
                        logger.info("Acceptance email sent to " + user.getEmail());
                    } catch (Exception e) {
                        logger.error("Error sending acceptance email to " + user.getEmail(), e);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "error", "message", "Error sending acceptance email."));
                    }
                }

                // Réponse structurée en cas de succès
                return ResponseEntity.ok(Map.of("status", "success", "message", "Instructor accepted and email sent."));
            } else {
                logger.error("User with ID " + idUser + " not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "error", "message", "User not found."));
            }
        } catch (Exception e) {
            logger.error("Error updating user status for user ID " + idUser, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "error", "message", "Error updating user status."));
        }
    }
    @PutMapping("/reject/{idUser}")
    public ResponseEntity<?> rejectInstructor(@PathVariable Long idUser, @RequestParam String rejectionReason) {
        try {
            logger.info("Attempting to reject instructor with ID: " + idUser);
            Optional<User> optionalUser = userRepository.findById(idUser);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                // Vérifier si l'utilisateur est déjà refusé
                if (Boolean.FALSE.equals(user.getAccepted())) {
                    logger.warn("User " + user.getFirstName() + " " + user.getLastName() + " is already rejected.");
                    return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "User is already rejected."));
                }

                // Vérifier si l'utilisateur est déjà accepté
                if (Boolean.TRUE.equals(user.getAccepted())) {
                    logger.warn("User " + user.getFirstName() + " " + user.getLastName() + " is already accepted.");
                    return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "User is already accepted."));
                }

                // Marquer l'utilisateur comme refusé
                user.setAccepted(false); // Définir accepted à false pour le refus
                userRepository.save(user);

                logger.info("User " + user.getFirstName() + " rejected successfully.");

                // Envoyer l'email de refus
                if (user.getEmail() != null) {
                    try {
                        emailService.sendRejectionEmail(user.getEmail(), user.getFirstName(), rejectionReason);
                        logger.info("Rejection email sent to " + user.getEmail());
                    } catch (Exception e) {
                        logger.error("Error sending rejection email to " + user.getEmail(), e);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "error", "message", "Error sending rejection email."));
                    }
                }

                // Réponse structurée en cas de succès
                return ResponseEntity.ok(Map.of("status", "success", "message", "Instructor rejected and email sent."));
            } else {
                logger.error("User with ID " + idUser + " not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "error", "message", "User not found."));
            }
        } catch (Exception e) {
            logger.error("Error updating user status for user ID " + idUser, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "error", "message", "Error updating user status."));
        }
    }
    private String saveCV(MultipartFile cv) {
        if (cv == null || cv.isEmpty()) {
            throw new RuntimeException("Le fichier CV est vide ou non fourni.");
        }

        String cvUploadDir = "uploads/cvs/";
        File directory = new File(cvUploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String contentType = cv.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            throw new RuntimeException("Seuls les fichiers PDF sont autorisés.");
        }

        // Générer un nom unique
        String fileName = UUID.randomUUID() + ".pdf";
        String filePath = cvUploadDir + fileName;

        try {
            Path destinationPath = Paths.get(cvUploadDir).resolve(fileName);
            Files.copy(cv.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("CV enregistré avec succès : {}", destinationPath.toString());
        } catch (IOException e) {
            log.error("Erreur lors de l'enregistrement du CV", e);
            throw new RuntimeException("Erreur lors de l'enregistrement du CV.", e);
        }

        return fileName; // Retourne le nom du fichier pour l'enregistrer en BD
    }

    @GetMapping("/cv/{filename:.+}")
    public ResponseEntity<Resource> getCV(@PathVariable String filename) {
        try {
            Path uploadsDir = Paths.get("uploads/cvs/").toAbsolutePath().normalize();
            Path filePath = uploadsDir.resolve(filename).normalize();

            // Vérification pour éviter les attaques de type path traversal
            if (!filePath.startsWith(uploadsDir)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Resource resource = new UrlResource(filePath.toUri());

            // Détection du type MIME du fichier
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/{idUser}/cv")
    public ResponseEntity<?> updateUserCV(@PathVariable Long idUser, @RequestParam("cv") MultipartFile cv) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Si un CV est déjà associé, le supprimer
        if (user.getCv() != null) {
            File oldCv = new File("uploads/cvs/" + user.getCv());
            if (oldCv.exists()) {
                oldCv.delete();
            }
        }

        // Enregistrer le nouveau CV
        if (cv != null && !cv.isEmpty()) {
            String cvPath = saveCV(cv);
            user.setCv(cvPath);
            userRepository.save(user);
            return ResponseEntity.ok(Map.of("message", "CV mis à jour avec succès !", "cvPath", cvPath, "user", user));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Aucun fichier fourni"));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOTP(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        if (email == null || otp == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email et OTP sont requis"));
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Utilisateur non trouvé"));
        }

        if (otp.equals(user.getOtp())) {
            // OTP valide, générer le token JWT
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            String token = jwtUtil.generateToken(userDetails);

            // Effacer l'OTP après une utilisation réussie
            user.setOtp(null);
            userRepository.save(user);

            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "OTP invalide"));
        }
    }

}