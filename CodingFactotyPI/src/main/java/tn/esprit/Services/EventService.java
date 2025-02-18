package tn.esprit.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.Repository.*;
import tn.esprit.entities.*;

import java.util.List;

@RequiredArgsConstructor
@Service

public class EventService implements IEventService{


    private final EventRepository eventRepository;
    private final FeedBackRepository feedBackEventRepository;
    private final LocationEventRepository locationEventRepository;
    private final NotificationRepository notificationRepository;
    private final PlanningRepository planningRepository;
    private final RegistrationRepository registrationRepository;

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

}
