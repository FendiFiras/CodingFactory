package tn.esprit.Services;

import tn.esprit.entities.BanLog;

import java.util.List;

public interface IBanLogService {
    BanLog addBanLog(BanLog banLog);
    List<BanLog> getAllBanLogs();
    BanLog modifyBanLog(BanLog banLog);
    void deleteBanLog(Long id);
    BanLog retrieveBanLog(Long id);
    List<BanLog> findByUserId(Long userId);
}