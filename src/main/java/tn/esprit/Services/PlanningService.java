package tn.esprit.Services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.Repository.*;
import tn.esprit.entities.Event;
import tn.esprit.entities.FeedBackEvent;
import tn.esprit.entities.LocationEvent;
import tn.esprit.entities.Planning;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

@RequiredArgsConstructor
@Service
public class PlanningService  implements IPlanningService {
    private final EventRepository eventRepository;
    private final FeedBackRepository feedBackEventRepository;
    private final LocationEventRepository locationEventRepository;
    private final NotificationRepository notificationRepository;
    private final PlanningRepository planningRepository;
    private final RegistrationRepository registrationRepository;


    public Planning addPlanning(Planning planning, Long idEvent, Long idLocationEvent) {
        Event e = eventRepository.findById(idEvent)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + idEvent));

        if (idLocationEvent != 0) {
            LocationEvent l = locationEventRepository.findById(idLocationEvent)
                    .orElseThrow(() -> new EntityNotFoundException("LocationEvent not found with ID: " + idLocationEvent));
            planning.setLocationEvent(l);
        }

        planning.setEvent(e);
        return planningRepository.save(planning);
    }

    public Planning updatePlanning(Planning planning,Long idEvent,Long idLocationEvent) {
        Event e = eventRepository.findById(idEvent)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + idEvent));

        if (idLocationEvent != 0) {
            LocationEvent l = locationEventRepository.findById(idLocationEvent)
                    .orElseThrow(() -> new EntityNotFoundException("LocationEvent not found with ID: " + idLocationEvent));
            planning.setLocationEvent(l);
        }
        planning.setEvent(e);
        return planningRepository.save(planning);

    }

    public Planning retrievePlanning(Long idPlanning) {
        return planningRepository.findById(idPlanning).get();
    }

    public void deletePlanning(Long idPlanning) {
        planningRepository.deleteById(idPlanning);

    }
    public List<Planning> getPlanning() {
        List<Planning> plannings = planningRepository.findAll();
        return plannings;
    }


    public List<Planning> getPlanningByEventId(Long eventId) {
        return planningRepository.findByEventId(eventId);
    }
}
