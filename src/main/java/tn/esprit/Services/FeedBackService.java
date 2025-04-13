package tn.esprit.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.Exceptions.CustomException;
import tn.esprit.Repository.*;
import tn.esprit.entities.Event;
import tn.esprit.entities.FeedBackEvent;
import tn.esprit.entities.Registration;
import tn.esprit.entities.User;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service

public class FeedBackService  implements IFeedBackEventService {

    private final EventRepository eventRepository;
    private final FeedBackRepository feedBackEventRepository;
    private final LocationEventRepository locationEventRepository;
    private final NotificationRepository notificationRepository;
    private final PlanningRepository planningRepository;
    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;


    public FeedBackEvent addFeedBackEvent(FeedBackEvent feedBackEvent, Long idEvent, Long idUser) {

        Event e = eventRepository.findById(idEvent)
                .orElseThrow(() -> new CustomException("Événement non trouvé"));

        User u = userRepository.findById(idUser)
                .orElseThrow(() -> new CustomException("Utilisateur non trouvé"));
        feedBackEvent.setEvent(e);
        feedBackEvent.setUser(u);
        feedBackEvent.setFeedbackDate(LocalDateTime.now());

        return feedBackEventRepository.save(feedBackEvent);

    }
    public FeedBackEvent updateFeedBackEvent(FeedBackEvent feedBackEvent) {

        return feedBackEventRepository.save(feedBackEvent);

    }

    public FeedBackEvent retrieveFeedBackEvent(Long idFeedBackEvent) {
        return feedBackEventRepository.findById(idFeedBackEvent).get();
    }


    public List<FeedBackEvent> getFeedBackEvent() {
        List<FeedBackEvent> feedBackEvents = feedBackEventRepository.findAll();
        return feedBackEvents;
    }

    public List<FeedBackEvent> getFeedbacksByEventId(Long eventId) {
        return feedBackEventRepository.findByEventId(eventId);
    }

    public void deleteFeedBackEvent(Long idFeedBackEvent) {
        feedBackEventRepository.deleteById(idFeedBackEvent);

    }


}
