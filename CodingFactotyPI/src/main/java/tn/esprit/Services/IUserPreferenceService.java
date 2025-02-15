package tn.esprit.Services;

import tn.esprit.entities.UserPreference;

import java.util.List;

public interface IUserPreferenceService {
    UserPreference addUserPreference(UserPreference userPreference);
    List<UserPreference> getAllUserPreferences();
    UserPreference modifyUserPreference(UserPreference userPreference);
    void deleteUserPreference(Long id);
    UserPreference retrieveUserPreference(Long id);
}