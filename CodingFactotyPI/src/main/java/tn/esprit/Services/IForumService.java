package tn.esprit.Services;

import tn.esprit.entities.Forum;

import java.util.List;

public interface IForumService {
    Forum addForum(Forum forum, Long userId);
    void deleteForum(Long forumId);
    Forum updateForum(Forum forum);
    Forum getOneById(Long id);
    List<Forum> getAllForums();
}
