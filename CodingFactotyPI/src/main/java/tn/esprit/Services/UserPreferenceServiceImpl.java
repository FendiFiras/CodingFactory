package tn.esprit.Services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.Repository.UserRepository;
import tn.esprit.entities.User;
import tn.esprit.entities.UserPreference;
import tn.esprit.Repository.UserPreferenceRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class UserPreferenceServiceImpl implements IUserPreferenceService {

    private UserPreferenceRepository userPreferenceRepository;
    private UserRepository userRepository;
    @Override
    public UserPreference addUserPreference(UserPreference userPreference, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("Utilisateur non trouvé avec l'ID : " + userId)
        );

        // Associe la préférence à l'utilisateur
        userPreference.setUser(user);
        user.setUserPreference(userPreference); // Maintient la relation bidirectionnelle

        return userPreferenceRepository.save(userPreference);
    }

    @Override
    public UserPreference addUserPreference(UserPreference userPreference) {
        return null;
    }


    @Override
    public List<UserPreference> getAllUserPreferences() {
        return userPreferenceRepository.findAll();
    }

    @Override
    public UserPreference modifyUserPreference(UserPreference userPreference) {
        // Assurez-vous que l'utilisateur associé est correctement mis à jour
        return userPreferenceRepository.save(userPreference);
    }

    @Override
    public void deleteUserPreference(Long id) {
        UserPreference userPreference = userPreferenceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Préférence utilisateur non trouvée avec l'ID : " + id));

        // Dissocier la préférence de l'utilisateur sans supprimer User
        User user = userPreference.getUser();
        if (user != null) {
            user.setUserPreference(null);
            userRepository.save(user);
        }

        userPreferenceRepository.delete(userPreference);
    }


    @Override
    public UserPreference retrieveUserPreference(Long id) {
        return userPreferenceRepository.findById(id).orElse(null);
    }
}