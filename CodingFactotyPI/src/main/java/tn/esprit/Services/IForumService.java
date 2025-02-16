package tn.esprit.Services;

import tn.esprit.entities.Forum;
import java.util.List;

public interface IForumService {
    List<Forum> getAllForums();
    Forum getForumById(Long id);
    Forum createForum(Forum forum);
    Forum updateForum(Long id, Forum forum);
    void deleteForum(Long id);
}
