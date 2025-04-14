package tn.esprit.controllers;

import lombok.RequiredArgsConstructor;
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
@CrossOrigin(origins = "*") // Allow requests from Angular (CORS)
@RestController
@RequestMapping("/offers")
@RequiredArgsConstructor
public class OfferController {


    private final IOfferService offerService;


    @PostMapping("/create/{userId}")
    public ResponseEntity<?> createOffer(@RequestBody Offer offer, @PathVariable Long userId) {
        try {
            Offer createdOffer = offerService.createOffer(offer, userId);
            return ResponseEntity.ok(createdOffer);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }



    @GetMapping("/companyrepresentative/{userId}")
    public ResponseEntity<List<Offer>> getOffersByCompanyRepresentative(@PathVariable Long userId) {
        List<Offer> offers = offerService.getOffersByCompanyRepresentative(userId);
        return ResponseEntity.ok(offers);
    }

    @GetMapping
    public ResponseEntity<List<Offer>> getAllOffers() {
        return ResponseEntity.ok(offerService.getAllOffers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Offer>> getOfferById(@PathVariable Long id) {
        return ResponseEntity.ok(offerService.getOfferById(id));
    }

    @PutMapping("/updateoffer/{id}")
    public ResponseEntity<Offer> updateOffer(@PathVariable Long id, @RequestBody Offer offer) {
        return ResponseEntity.ok(offerService.updateOffer(id, offer));
    }

    @DeleteMapping("/deleteoffer/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
        offerService.deleteOffer(id);
        return ResponseEntity.noContent().build();
    }
}
