package tn.esprit.Services;

import tn.esprit.entities.Discussion;

public interface IDiscussionService {
    Discussion addDiscussion(Discussion discussion, Long forum_id);
}
