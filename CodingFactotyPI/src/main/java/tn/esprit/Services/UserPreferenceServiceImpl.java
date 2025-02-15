package tn.esprit.Services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.entities.UserPreference;
import tn.esprit.Repository.UserPreferenceRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class UserPreferenceServiceImpl implements IUserPreferenceService {

    private UserPreferenceRepository userPreferenceRepository;

    @Override
    public UserPreference addUserPreference(UserPreference userPreference) {
        if (userPreference.getUser() == null) {
            throw new IllegalArgumentException("Chaque préférence doit être liée à un utilisateur.");
        }
        return userPreferenceRepository.save(userPreference);
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
        // Supprime la préférence, mais assurez-vous que l'utilisateur associé est géré correctement
        userPreferenceRepository.deleteById(id);
    }

    @Override
    public UserPreference retrieveUserPreference(Long id) {
        return userPreferenceRepository.findById(id).orElse(null);
    }
}