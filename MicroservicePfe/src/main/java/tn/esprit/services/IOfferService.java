package tn.esprit.services;

import tn.esprit.entities.Offer;
import tn.esprit.entities.User;

import java.util.List;
import java.util.Optional;

public interface IOfferService {
    public Offer createOffer(Offer offer, Long userId) ;
    public List<Offer> getOffersByCompanyRepresentative(Long userId);
    List<Offer> getAllOffers();
    Optional<Offer> getOfferById(Long id);
    Offer updateOffer(Long id, Offer offer);
    void deleteOffer(Long id);
}