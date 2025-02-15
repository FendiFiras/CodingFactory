package tn.esprit.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.entities.LoginHistory;
import tn.esprit.Services.ILoginHistoryService;

import java.util.List;

@RestController
@RequestMapping("/login-histories")
@AllArgsConstructor
public class LoginHistoryController {

    private ILoginHistoryService loginHistoryService;

    @PostMapping
    public LoginHistory addLoginHistory(@RequestBody LoginHistory loginHistory) {
        return loginHistoryService.addLoginHistory(loginHistory);
    }

    @GetMapping
    public List<LoginHistory> getAllLoginHistories() {
        return loginHistoryService.getAllLoginHistories();
    }

    @PutMapping
    public LoginHistory modifyLoginHistory(@RequestBody LoginHistory loginHistory) {
        return loginHistoryService.modifyLoginHistory(loginHistory);
    }

    @DeleteMapping("/{id}")
    public void deleteLoginHistory(@PathVariable Long id) {
        loginHistoryService.deleteLoginHistory(id);
    }

    @GetMapping("/{id}")
    public LoginHistory retrieveLoginHistory(@PathVariable Long id) {
        return loginHistoryService.retrieveLoginHistory(id);
    }

    @GetMapping("/user/{userId}")
    public List<LoginHistory> findByUserId(@PathVariable Long userId) {
        return loginHistoryService.findByUserId(userId); // Appel du service
    }
}