package tn.esprit.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.entities.Partnership;
import tn.esprit.entities.Offer;
import tn.esprit.entities.Application;
import tn.esprit.entities.User;
import tn.esprit.services.IPartnershipService;
import tn.esprit.services.IOfferService;
import tn.esprit.services.IApplicationService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:4200") // Allow requests from Angular app
@RestController
@RequestMapping("/partnerships")
@RequiredArgsConstructor
public class PartnershipController {
    private final IPartnershipService partnershipService;


    @PostMapping("/apply/{userId}")
    public ResponseEntity<?> applyForPartnership(@RequestBody Partnership partnership, @PathVariable Long userId) {
        try {
            Partnership savedPartnership = partnershipService.applyForPartnership(partnership, userId);
            return ResponseEntity.ok(savedPartnership);
        } catch (RuntimeException e) {
            // Return error message properly
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", e.getMessage()));
        }
    }




    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUserDetails(@PathVariable Long userId) {
        User user = partnershipService.getUserDetails(userId);
        return ResponseEntity.ok(user);
    }


    @GetMapping("/Get")
    public ResponseEntity<?> getAllPartnerships() {
        try {
            List<Partnership> partnerships = partnershipService.getAllPartnerships();
            return ResponseEntity.ok(partnerships);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<Optional<Partnership>> getPartnershipById(@PathVariable Long id) {
        return ResponseEntity.ok(partnershipService.getPartnershipById(id));
    }

    @PutMapping("/update-partnership/{id}")
    public ResponseEntity<Partnership> updatePartnership(@PathVariable Long id, @RequestBody Partnership partnership) {
        return ResponseEntity.ok(partnershipService.updatePartnership(id, partnership));
    }

    @DeleteMapping("/delete-partnership/{id}")
    public ResponseEntity<Void> deletePartnership(@PathVariable Long id) {
        partnershipService.deletePartnership(id);
        return ResponseEntity.noContent().build();
    }
}