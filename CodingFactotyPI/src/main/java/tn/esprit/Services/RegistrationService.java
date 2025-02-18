package tn.esprit.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.Repository.*;
import tn.esprit.entities.Event;
import tn.esprit.entities.LocationEvent;
import tn.esprit.entities.Registration;
import tn.esprit.entities.User;

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


    public Registration addRegistration(Registration registration, Long idEvent, Long idUser) {

        Event e=eventRepository.getById(idEvent);
        User u=userRepository.getById(idUser);
        registration.setRevent(e);
        registration.setUser(u);
        return registrationRepository.save(registration);

    }
    public Registration updateRegistration(Registration registration) {

        return registrationRepository.save(registration);

    }

    public Registration retrieveRegistration(Long idRegistration) {
        return registrationRepository.findById(idRegistration).get();
    }

    public void deleteRegistration(Long idRegistration) {
        registrationRepository.deleteById(idRegistration);

    }
    public List<Registration> getRegistration() {
        List<Registration> registrations = registrationRepository.findAll();
        return registrations;
    }


}
