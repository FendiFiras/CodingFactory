package tn.esprit.Controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.Configuration.JwtUtil;
import tn.esprit.Repository.UserRepository;
import tn.esprit.entities.User;
import tn.esprit.entities.Gender;
import tn.esprit.entities.Role;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

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
            @RequestParam(value = "cv", required = false) String cv,
            @RequestParam(value = "speciality", required = false) String speciality,
            @RequestParam(value = "companyName", required = false) String companyName,
            @RequestParam(value = "level", required = false) String level,
            @RequestParam(value = "grade", required = false) String grade,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam("role") String role) {

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // Hash du mot de passe
        user.setDateOfBirth(dateOfBirth); // Stocke la date sans l'heure
        user.setGender(Gender.valueOf(gender));
        user.setPhoneNumber(phoneNumber);
        user.setCv(cv);
        user.setSpeciality(speciality);
        user.setCompanyName(companyName);
        user.setLevel(level);
        user.setGrade(grade);
        user.setAddress(address);
        user.setRole(Role.valueOf(role)); // Convertir le rôle à partir du String

        // Sauvegarde de l'image
        if (image != null && !image.isEmpty()) {
            String imagePath = saveImage(image);
            user.setImage(imagePath);
        }

        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Utilisateur enregistré avec succès !"));
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



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
            String token = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(Map.of("token", token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Email ou mot de passe incorrect"));
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
}
