package tn.esprit.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.entities.Forum;
import tn.esprit.Repository.ForumRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ForumService implements IForumService {

    @Autowired
    private ForumRepository forumRepository;

    @Override
    public List<Forum> getAllForums() {
        return forumRepository.findAll();
    }

    @Override
    public Forum getForumById(Long id) {
        return forumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Forum not found with id: " + id));
    }

    @Override
    public Forum createForum(Forum forum) {
        return forumRepository.save(forum);
    }

    @Override
    public Forum updateForum(Long id, Forum forum) {
        Optional<Forum> existingForum = forumRepository.findById(id);
        if (existingForum.isPresent()) {
            Forum updatedForum = existingForum.get();
            updatedForum.setTitle(forum.getTitle());
            updatedForum.setDescription(forum.getDescription());
            updatedForum.setImage(forum.getImage());
            return forumRepository.save(updatedForum);
        } else {
            throw new RuntimeException("Forum not found with id: " + id);
        }
    }

    @Override
    public void deleteForum(Long id) {
        if (forumRepository.existsById(id)) {
            forumRepository.deleteById(id);
        } else {
            throw new RuntimeException("Forum not found with id: " + id);
        }
    }
}
