package tn.esprit.Services;

import tn.esprit.entities.BanLog;

import java.util.List;

public interface IBanLogService {
    BanLog addBanLog(BanLog banLog);
    BanLog addBanLog(Long userId, BanLog banLog); // Nouvelle méthode
    List<BanLog> getAllBanLogs();


    BanLog modifyBanLog(Long id, BanLog updatedBanLog);

    void deleteBanLog(Long id);
    BanLog retrieveBanLog(Long id);
    List<BanLog> findByUserId(Long userId);
}