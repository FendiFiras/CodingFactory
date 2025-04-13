package tn.esprit.Services;

import tn.esprit.entities.LoginHistory;

import java.util.List;

public interface ILoginHistoryService {
    LoginHistory addLoginHistory(LoginHistory loginHistory);
    List<LoginHistory> getAllLoginHistories();
    LoginHistory modifyLoginHistory(LoginHistory loginHistory);
    void deleteLoginHistory(Long id);
    LoginHistory retrieveLoginHistory(Long id);
    List<LoginHistory> findByUserId(Long userId);
}