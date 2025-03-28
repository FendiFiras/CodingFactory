package tn.esprit.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.Exceptions.CustomException;
import tn.esprit.Repository.*;
import tn.esprit.entities.*;

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
    private final SimpMessagingTemplate messagingTemplate;


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
        Registration savedRegistration = registrationRepository.save(registration);
        // 🔥 Envoi de la mise à jour via WebSocket
        long participantCount = registrationRepository.countConfirmedParticipantsByEventId(idEvent);
        messagingTemplate.convertAndSend("/topic/event/" + idEvent, participantCount);

        return savedRegistration;
    }
    public Registration updateRegistration(Registration registration) {

        return registrationRepository.save(registration);

    }

    public Registration retrieveRegistration(Long idRegistration) {
        return registrationRepository.findById(idRegistration).get();
    }

    public void deleteRegistration(Long idRegistration) {
        Registration r = registrationRepository.findById(idRegistration)
                .orElseThrow(() -> new CustomException("Événement non trouvé"));


        registrationRepository.deleteById(idRegistration);
        long participantCount = registrationRepository.countConfirmedParticipantsByEventId(r.getEvent().getIdEvent());
        messagingTemplate.convertAndSend("/topic/event/" + r.getEvent().getIdEvent(), participantCount);

    }
    public List<Registration> getRegistration() {
        List<Registration> registrations = registrationRepository.findAll();
        return registrations;
    }
    public long getParticipantCountByEventId(Long idEvent) {
        return registrationRepository.countConfirmedParticipantsByEventId(idEvent);
    }


    public List<Registration> getRegistrationByEventId(Long eventId) {
        return registrationRepository.findByEventId(eventId);
    }


   public boolean dejaparticiper(Long idEvent,Long idUser)
   {

       Event e = eventRepository.findById(idEvent)
               .orElseThrow(() -> new CustomException("Événement non trouvé"));

       User u = userRepository.findById(idUser)
               .orElseThrow(() -> new CustomException("Utilisateur non trouvé"));
       // Vérifier si l'utilisateur est déjà inscrit
       boolean isAlreadyRegistered = registrationRepository.existsByEventAndUser(e, u);
       return (isAlreadyRegistered);

   }
}
