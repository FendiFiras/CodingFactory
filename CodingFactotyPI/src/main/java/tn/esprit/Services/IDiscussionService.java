package tn.esprit.Services;

import tn.esprit.entities.Discussion;
import java.util.List;

public interface IDiscussionService {
    Discussion addDiscussion(Discussion discussion, Long forum_id);
    Discussion getDiscussionById(Long discussionId);
    List<Discussion> getAllDiscussions();
    Discussion updateDiscussion(Long discussionId, Discussion discussion);
    void deleteDiscussion(Long discussionId);

}
