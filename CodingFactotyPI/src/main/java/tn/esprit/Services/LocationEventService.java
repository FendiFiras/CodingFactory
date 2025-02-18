package tn.esprit.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.Repository.*;
import tn.esprit.entities.*;

import java.util.List;

@RequiredArgsConstructor
@Service

public class LocationEventService  implements ILocationEventService {
    private final EventRepository eventRepository;
    private final FeedBackRepository feedBackEventRepository;
    private final LocationEventRepository locationEventRepository;
    private final NotificationRepository notificationRepository;
    private final PlanningRepository planningRepository;
    private final RegistrationRepository registrationRepository;

    public LocationEvent addLocationEvent(LocationEvent locationEvent) {

        return locationEventRepository.save(locationEvent);

    }

    public LocationEvent updateLocationEvent(LocationEvent locationEvent) {


        return locationEventRepository.save(locationEvent);

    }

    public LocationEvent retrieveLocationEvent(Long idLocationEvent) {

        return locationEventRepository.findById(idLocationEvent).get();
    }

    public void deleteLocationEvent(Long idLocationEvent) {
        locationEventRepository.deleteById(idLocationEvent);

    }
    public List<LocationEvent> getLocationEvent() {
        List<LocationEvent> locationEvent = locationEventRepository.findAll();
        return locationEvent;
    }

}
