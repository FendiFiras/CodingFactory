package tn.esprit.Services;

import tn.esprit.entities.FeedBackEvent;

import java.util.List;

public interface IFeedBackEventService {

    public FeedBackEvent addFeedBackEvent(FeedBackEvent feedBackEvent, Long idEvent, Long idUser);

    public List<FeedBackEvent> getFeedbacksByEventId(Long eventId);


    }
