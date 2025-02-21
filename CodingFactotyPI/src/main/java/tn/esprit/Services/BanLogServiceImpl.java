package tn.esprit.Services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.Repository.UserRepository;
import tn.esprit.entities.BanLog;
import tn.esprit.Repository.BanLogRepository;
import tn.esprit.entities.User;

import java.util.List;

@AllArgsConstructor
@Service
public class BanLogServiceImpl implements IBanLogService {

    private BanLogRepository banLogRepository;
    private UserRepository userRepository;


    @Override
    public BanLog addBanLog(BanLog banLog) {
        return banLogRepository.save(banLog);
    }

    @Override
    public BanLog addBanLog(Long userId, BanLog banLog) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("Utilisateur non trouvé avec l'ID : " + userId)
        );
        banLog.setUser(user);
        return banLogRepository.save(banLog);
    }


    @Override
    public BanLog modifyBanLog(Long id, BanLog updatedBanLog) {
        return banLogRepository.findById(id)
                .map(existingBanLog -> {
                    existingBanLog.setBanDuration(updatedBanLog.getBanDuration());
                    existingBanLog.setBanReason(updatedBanLog.getBanReason());
                    existingBanLog.setStatus(updatedBanLog.getStatus());
                    return banLogRepository.save(existingBanLog);
                }).orElseThrow(() -> new RuntimeException("BanLog not found with id " + id));
    }


    @Override
    public void deleteBanLog(Long id) {
        banLogRepository.deleteById(id);
    }

    @Override
    public BanLog retrieveBanLog(Long id) {
        return banLogRepository.findById(id).orElse(null);
    }

    @Override
    public List<BanLog> findByUserId(Long userId) {
        List<BanLog> banLogs = banLogRepository.findByUser_IdUser(userId);
        if (banLogs.isEmpty()) {
            throw new IllegalArgumentException("Aucun BanLog trouvé pour l'utilisateur avec ID : " + userId);
        }
        return banLogs;
    }

    public List<BanLog> getAllBanLogs() {
        return banLogRepository.findAllWithUser();
    }



}