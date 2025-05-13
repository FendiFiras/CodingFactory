package tn.esprit.Services;

import tn.esprit.entities.Discussion;

import java.util.List;

public interface IDiscussionService {
    public Discussion addDiscussion(Discussion discussion, Long userId, Long forumId) ;     void deleteDiscussion(Long discussionId);
    Discussion updateDiscussion(Discussion discussion);
    Discussion getOneById(Long id);
    List<Discussion> getAllDiscussions();

    List<Discussion> getDiscussionsByForum(Long forumId);
}
