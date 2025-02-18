package tn.esprit.Services;

import tn.esprit.entities.Discussion;

import java.util.List;

public interface IDiscussionService {
    Discussion addDiscussion(Discussion discussion, Long userId);
    void deleteDiscussion(Long discussionId);
    Discussion updateDiscussion(Discussion discussion);
    Discussion getOneById(Long id);
    List<Discussion> getAllDiscussions();
}
