package tn.esprit.Services;

import tn.esprit.entities.Event;
import tn.esprit.entities.Registration;
import tn.esprit.entities.User;

import java.util.List;

public interface IRegistrationService {


    public Registration addRegistration(Registration registration, Long idEvent, Long idUser);
    public Registration updateRegistration(Registration registration);
    public Registration retrieveRegistration(Long idRegistration) ;
    public void deleteRegistration(Long idRegistration);
    public List<Registration> getRegistration();
    public long getParticipantCountByEventId(Long idEvent);
    public List<Registration> getRegistrationByEventId(Long eventId);
    public boolean dejaparticiper(Long idEvent,Long idUser);




}
