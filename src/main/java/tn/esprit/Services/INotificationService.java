package tn.esprit.Services;

import tn.esprit.entities.Event;
import tn.esprit.entities.Notification;

import java.util.List;

public interface INotificationService {


    public Notification addNotification(Notification notification, Long idEvent);
    public Notification updateNotification(Notification notification);
    public Notification retrieveNotification(Long idNotification);
    public void deleteNotification(Long idNotification);
    public List<Notification> getNotification();
}
