package tn.esprit.Services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.entities.BanLog;
import tn.esprit.Repository.BanLogRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class BanLogServiceImpl implements IBanLogService {

    private BanLogRepository banLogRepository;

    @Override
    public BanLog addBanLog(BanLog banLog) {
        return banLogRepository.save(banLog);
    }

    @Override
    public List<BanLog> getAllBanLogs() {
        return banLogRepository.findAll();
    }

    @Override
    public BanLog modifyBanLog(BanLog banLog) {
        return banLogRepository.save(banLog);
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
        return banLogRepository.findByUser_IdUser(userId); // Utilisation de la méthode corrigée
    }
}