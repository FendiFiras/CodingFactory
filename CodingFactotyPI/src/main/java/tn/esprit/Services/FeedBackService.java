package tn.esprit.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.Repository.*;
import tn.esprit.entities.Event;
import tn.esprit.entities.FeedBackEvent;
import tn.esprit.entities.Registration;
import tn.esprit.entities.User;

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

        Event e=eventRepository.getById(idEvent);
        User u=userRepository.getById(idUser);
        feedBackEvent.setFeedEvent(e);
        feedBackEvent.setUser(u);
        return feedBackEventRepository.save(feedBackEvent);

    }
    public FeedBackEvent updateFeedBackEvent(FeedBackEvent feedBackEvent) {

        return feedBackEventRepository.save(feedBackEvent);

    }

    public FeedBackEvent retrieveFeedBackEvent(Long idFeedBackEvent) {
        return feedBackEventRepository.findById(idFeedBackEvent).get();
    }

    public void deleteFeedBackEvent(Long idFeedBackEvent) {
        feedBackEventRepository.deleteById(idFeedBackEvent);

    }
    public List<FeedBackEvent> getFeedBackEvent() {
        List<FeedBackEvent> feedBackEvents = feedBackEventRepository.findAll();
        return feedBackEvents;
    }





}
