package tn.esprit.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.Repository.UserPreferenceRepository;
import tn.esprit.entities.UserPreference;
import tn.esprit.Services.IUserPreferenceService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/userPreferences")
@AllArgsConstructor
public class UserPreferenceController {

    private IUserPreferenceService userPreferenceService;

    @Autowired
    private UserPreferenceRepository userPreferenceRepository;
    @PostMapping("/{userId}")
    public ResponseEntity<?> addUserPreference(@RequestBody UserPreference userPreference, @PathVariable(required = false) Long userId) {
        if (userId == null) {
            return ResponseEntity.badRequest().body("L'ID utilisateur est requis");
        }
        UserPreference savedPreference = userPreferenceService.addUserPreference(userPreference, userId);
        return ResponseEntity.ok(savedPreference);
    }

    @GetMapping
    public List<UserPreference> getAllUserPreferences() {
        return userPreferenceService.getAllUserPreferences();
    }

    @PutMapping("/{id}")
    public UserPreference modifyUserPreference(@PathVariable Long id, @RequestBody UserPreference userPreference) {
        // Vérifie si l'ID dans l'URL et l'ID dans l'objet utilisateur sont cohérents
        if (!userPreference.getIdPreference().equals(id)) {
            throw new IllegalArgumentException("L'ID dans l'URL ne correspond pas à l'ID dans l'objet.");
        }
        return userPreferenceService.modifyUserPreference(id, userPreference);
    }



    @DeleteMapping("/{id}")
    public void deleteUserPreference(@PathVariable Long id) {
        userPreferenceService.deleteUserPreference(id);
    }

    @GetMapping("/{idUser}")
    public ResponseEntity<?> retrieveUserPreference(@PathVariable Long idUser) {
        UserPreference preference = userPreferenceService.retrieveUserPreference(idUser);
        return preference != null ? ResponseEntity.ok(preference) : ResponseEntity.notFound().build();
    }
    @GetMapping("/stats/theme")
    public Object getThemeStats() {
        long darkModeCount = userPreferenceRepository.countByTheme("dark");
        long lightModeCount = userPreferenceRepository.countByTheme("light");

        return new Object() {
            public long darkMode = darkModeCount;
            public long lightMode = lightModeCount;
        };
    }

}