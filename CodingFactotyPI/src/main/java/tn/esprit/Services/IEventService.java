package tn.esprit.Services;

import tn.esprit.entities.Event;
import tn.esprit.entities.Role;
import tn.esprit.entities.User;

import java.util.List;

public interface IEventService {
    public Event addEvent(Event event) ;
    public Event updateEvent(Event event);
    public Event retrieveEvent(Long idEvent);

    public void deleteEvent(Long idEvent);
    public List<Event> getEvent();


}

