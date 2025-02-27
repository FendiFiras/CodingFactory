package tn.esprit.services;

import tn.esprit.entities.Application;
import tn.esprit.entities.Partnership;

import java.util.List;
import java.util.Optional;

public interface IApplicationService {
    public Application applyForOffer(Application application, Long userId);public List<Application> getApplicationsByOfferId(Long offerId);
    public Long getUserIdByApplicationId(Long applicationId) ;
    public List<Application> getApplicationsForCompanyRepresentative(Long userId);
    public List<Application> getApplicationsByStudent(Long userId);
    List<Application> getAllApplications();
    Optional<Application> getApplicationById(Long id);
    Application updateApplication(Long id, Application application);
    void deleteApplication(Long id);
}