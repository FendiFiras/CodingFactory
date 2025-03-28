package tn.esprit.Services;

import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.Repository.*;
import tn.esprit.entities.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;



@RequiredArgsConstructor
@Service

public class EventService implements IEventService{


    private final EventRepository eventRepository;
    private final FeedBackRepository feedBackEventRepository;
    private final LocationEventRepository locationEventRepository;
    private final NotificationRepository notificationRepository;
    private final PlanningRepository planningRepository;
    private final RegistrationRepository registrationRepository;
    private final QRCodeService qrCodeService;

    public Event addEvent(Event event) {
       // User admin = userRepository.findByRole(Role.ADMIN); // Récupérer l’admin

       // event.setUser(admin); // Assigner l'admin à l'événement
        return eventRepository.save(event);

    }

    public Event updateEvent(Event event) {


        return eventRepository.save(event);

    }

    public Event retrieveEvent(Long idEvent) {

        return eventRepository.findById(idEvent).get();
    }

    public void deleteEvent(Long idEvent) {
        eventRepository.deleteById(idEvent);

    }
    public List<Event> getEvent() {
        List<Event> event = eventRepository.findAll();
        return event;
    }

    // 🎯 **Méthode pour générer le QR Code d'un événement**
    public byte[] generateEventQRCode(Long eventId) throws WriterException, IOException {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("L'événement avec ID " + eventId + " n'existe pas."));

        // Utilisez une URL qui pointe vers une route fonctionnelle dans Angular
        String qrContent = "http://localhost:4200/detailseventfront/" + eventId; // Modifiez pour une route existante
        return qrCodeService.generateQRCode(qrContent, 300, 300);
    }
    // Méthode de recherche multi-critères utilisant le paramètre "s"
    public List<Event> searchEvents(String s) {
        return eventRepository.searchEvents(s);
    }

}
