package tn.esprit.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.Exceptions.CustomException;
import tn.esprit.Repository.*;
import tn.esprit.entities.Event;
import tn.esprit.entities.LocationEvent;
import tn.esprit.entities.Registration;
import tn.esprit.entities.User;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service

public class RegistrationService  implements IRegistrationService{

    private final EventRepository eventRepository;
    private final FeedBackRepository feedBackEventRepository;
    private final LocationEventRepository locationEventRepository;
    private final NotificationRepository notificationRepository;
    private final RegistrationRepository registrationRepository;
    private final PlanningRepository planningRepository;
    private final UserRepository userRepository;


    public Registration addRegistration(Registration registration, Long idEvent, Long idUser) {
        Event e = eventRepository.findById(idEvent)
                .orElseThrow(() -> new CustomException("Événement non trouvé"));

        User u = userRepository.findById(idUser)
                .orElseThrow(() -> new CustomException("Utilisateur non trouvé"));

        // Vérifier si l'événement est complet
        long currentParticipants = registrationRepository.countByEvent(e);
        if (currentParticipants >= e.getMaxParticipants()) {
            throw new CustomException("L'événement est complet, inscription impossible.");
        }

        // Vérifier si l'utilisateur est déjà inscrit
        boolean isAlreadyRegistered = registrationRepository.existsByEventAndUser(e, u);
        if (isAlreadyRegistered) {
            throw new CustomException("Vous êtes déjà inscrit à cet événement.");
        }

        // Vérifier si la date limite d'inscription est dépassée
        if (LocalDateTime.now().isAfter(e.getRegistrationDeadline())) {
            throw new CustomException("La date limite d'inscription est dépassée.");
        }

        // Ajouter l'inscription
        registration.setEvent(e);
        registration.setUser(u);
        registration.setRegistrationDate(LocalDateTime.now()); // 🔹 Mettre la date d'aujourd'hui
        registration.setConfirmation(true);

        return registrationRepository.save(registration);
    }
    public Registration updateRegistration(Registration registration) {

        return registrationRepository.save(registration);

    }

    public Registration retrieveRegistration(Long idRegistration) {
        return registrationRepository.findById(idRegistration).get();
    }

    public void deleteRegistration(Long idRegistration) {
        registrationRepository.deleteById(idRegistration);

    }
    public List<Registration> getRegistration() {
        List<Registration> registrations = registrationRepository.findAll();
        return registrations;
    }
    public long getParticipantCountByEventId(Long idEvent) {
        return registrationRepository.countConfirmedParticipantsByEventId(idEvent);
    }

}
