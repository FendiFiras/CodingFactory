package tn.esprit.Services;

import com.google.zxing.WriterException;
import tn.esprit.entities.Event;
import tn.esprit.entities.Role;
import tn.esprit.entities.User;

import java.io.IOException;
import java.util.List;

public interface IEventService {
    public Event addEvent(Event event) ;
    public Event updateEvent(Event event);
    public Event retrieveEvent(Long idEvent);

    public void deleteEvent(Long idEvent);
    public List<Event> getEvent();

    byte[] generateEventQRCode(Long eventId) throws WriterException, IOException;

    public List<Event> searchEvents(String s);


    }

