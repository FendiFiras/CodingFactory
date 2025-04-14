package tn.esprit.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.Repository.*;
import tn.esprit.entities.Event;
import tn.esprit.entities.LocationEvent;
import tn.esprit.entities.Notification;
import tn.esprit.entities.Planning;

import java.util.List;

@RequiredArgsConstructor
@Service

public class NotificationService  implements INotificationService{
    private final EventRepository eventRepository;
    private final FeedBackRepository feedBackEventRepository;
    private final LocationEventRepository locationEventRepository;
    private final NotificationRepository notificationRepository;
    private final PlanningRepository planningRepository;
    private final RegistrationRepository registrationRepository;

    public Notification addNotification(Notification notification, Long idEvent) {

        Event e=eventRepository.getById(idEvent);
        notification.setEvent(e);
        return notificationRepository.save(notification);

    }
    public Notification updateNotification(Notification notification) {


        return notificationRepository.save(notification);

    }

    public Notification retrieveNotification(Long idNotification) {
        return notificationRepository.findById(idNotification).get();
    }

    public void deleteNotification(Long idNotification) {
        notificationRepository.deleteById(idNotification);

    }
    public List<Notification> getNotification() {
        List<Notification> notifications = notificationRepository.findAll();
        return notifications;
    }


}
