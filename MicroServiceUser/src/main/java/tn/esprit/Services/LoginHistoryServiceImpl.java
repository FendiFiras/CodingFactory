package tn.esprit.Services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.entities.LoginHistory;
import tn.esprit.Repository.LoginHistoryRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class LoginHistoryServiceImpl implements ILoginHistoryService {

    private LoginHistoryRepository loginHistoryRepository;

    @Override
    public LoginHistory addLoginHistory(LoginHistory loginHistory) {
        return loginHistoryRepository.save(loginHistory);
    }

    @Override
    public List<LoginHistory> getAllLoginHistories() {
        return loginHistoryRepository.findAll();
    }

    @Override
    public LoginHistory modifyLoginHistory(LoginHistory loginHistory) {
        return loginHistoryRepository.save(loginHistory);
    }

    @Override
    public void deleteLoginHistory(Long id) {
        loginHistoryRepository.deleteById(id);
    }

    @Override
    public LoginHistory retrieveLoginHistory(Long id) {
        return loginHistoryRepository.findById(id).orElse(null);
    }

    @Override
    public List<LoginHistory> findByUserId(Long userId) {
        return loginHistoryRepository.findByUser_IdUser(userId); // Appel de la méthode corrigée
    }
}