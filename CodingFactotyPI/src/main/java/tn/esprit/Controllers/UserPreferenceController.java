package tn.esprit.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.entities.UserPreference;
import tn.esprit.Services.IUserPreferenceService;

import java.util.List;

@RestController
@RequestMapping("/userPreferences")
@AllArgsConstructor
public class UserPreferenceController {

    private IUserPreferenceService userPreferenceService;

    @PostMapping("/{userId}")
    public UserPreference addUserPreference(@RequestBody UserPreference userPreference, @PathVariable Long userId) {
        return userPreferenceService.addUserPreference(userPreference, userId);
    }

    @GetMapping
    public List<UserPreference> getAllUserPreferences() {
        return userPreferenceService.getAllUserPreferences();
    }

    @PutMapping
    public UserPreference modifyUserPreference(@RequestBody UserPreference userPreference) {
        return userPreferenceService.modifyUserPreference(userPreference);
    }

    @DeleteMapping("/{id}")
    public void deleteUserPreference(@PathVariable Long id) {
        userPreferenceService.deleteUserPreference(id);
    }

    @GetMapping("/{id}")
    public UserPreference retrieveUserPreference(@PathVariable Long id) {
        return userPreferenceService.retrieveUserPreference(id);
    }
}