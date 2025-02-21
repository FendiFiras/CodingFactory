package tn.esprit.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.entities.BanLog;
import tn.esprit.Services.IBanLogService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/ban-logs")
@AllArgsConstructor
public class BanLogController {

    private IBanLogService banLogService;

    @PostMapping("/{userId}")
    public BanLog addBanLog(@PathVariable Long userId, @RequestBody BanLog banLog) {
        return banLogService.addBanLog(userId, banLog);
    }


    @GetMapping
    public List<BanLog> getAllBanLogs() {
        return banLogService.getAllBanLogs();
    }
    @PutMapping("/{id}")
    public BanLog modifyBanLog(@PathVariable Long id, @RequestBody BanLog banLog) {
        System.out.println("Modification du BanLog avec ID: " + id); // Debug
        return banLogService.modifyBanLog(id, banLog);
    }


    @DeleteMapping("/{id}")
    public void deleteBanLog(@PathVariable Long id) {
        banLogService.deleteBanLog(id);
    }

    @GetMapping("/{id}")
    public BanLog retrieveBanLog(@PathVariable Long id) {
        return banLogService.retrieveBanLog(id);
    }

    @GetMapping("/user/{userId}")
    public List<BanLog> findByUserId(@PathVariable Long userId) {
        System.out.println("🔎 Recherche des bans pour l'utilisateur ID : " + userId);
        return banLogService.findByUserId(userId);
    }

}