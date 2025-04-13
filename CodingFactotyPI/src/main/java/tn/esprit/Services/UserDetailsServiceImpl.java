package tn.esprit.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tn.esprit.Repository.BanLogRepository;
import tn.esprit.Repository.UserRepository;
import tn.esprit.entities.BanLog;
import tn.esprit.entities.Gender;
import tn.esprit.entities.User;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final BanLogRepository banLogRepository; // ✅ Correction ici (minuscule)

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Vérifier si l'utilisateur est actuellement banni
        if (isUserBanned(user)) {
            throw new RuntimeException("Your account is banned until " + getBanEndDate(user));
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())  // Assure-toi que le mot de passe est bien haché dans la base
                .roles(user.getRole().name())  // Rôle de l'utilisateur
                .build();
    }

    // Rendre cette méthode publique
    public boolean isUserBanned(User user) {
        List<BanLog> banLogs = banLogRepository.findByUser_IdUser(user.getIdUser()); // ✅ Correction ici
        Timestamp now = new Timestamp(System.currentTimeMillis());

        return banLogs.stream()
                .anyMatch(ban -> ban.getBanDuration().after(now)); // ✅ Vérifie si un ban est encore actif
    }

    // Rendre cette méthode publique
    public Timestamp getBanEndDate(User user) {
        return banLogRepository.findByUser_IdUser(user.getIdUser()).stream()
                .map(BanLog::getBanDuration)
                .max(Timestamp::compareTo) // ✅ Prend la date la plus grande (dernier ban)
                .orElse(null);
    }
    public Map<String, Long> getGenderStatistics() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("Male", userRepository.countByGender(Gender.MALE));
        stats.put("Female", userRepository.countByGender(Gender.FEMALE));
        return stats;
    }

}