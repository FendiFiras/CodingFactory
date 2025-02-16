package tn.esprit.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.entities.Courses;
import tn.esprit.entities.Session;
import tn.esprit.entities.TrainingType;
import tn.esprit.repositories.CoursesRepo;
import tn.esprit.repositories.SessionRepo;

import java.util.List;

@Service
@AllArgsConstructor
public class ServiceSession  implements IServiceSession {

    SessionRepo sessionRepo;
    CoursesRepo coursesRepo;
    public Session addSession(Session session, Long courseId) {
        Courses course = coursesRepo.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));

        // Vérifier si le Training est bien ON_SITE
        if (course.getTraining().getType() != TrainingType.ON_SITE) {
            throw new IllegalArgumentException("Only courses with ON_SITE training can have a session.");
        }

        session.setCourses(course);
        return sessionRepo.save(session);
}


    public Session updateSession(Session session) {
        if (session.getSessionId() == null) {
            throw new IllegalArgumentException("Session ID is required for updating.");
        }

        Session existingSession = sessionRepo.findById(session.getSessionId())
                .orElseThrow(() -> new IllegalArgumentException("Session not found with ID: " + session.getSessionId()));

        // Vérifier que le cours associé existe et a un Training ON_SITE
        Courses course = existingSession.getCourses();
        if (course != null && course.getTraining().getType() != TrainingType.ON_SITE) {
            throw new IllegalArgumentException("Only sessions linked to ON_SITE courses can be updated.");
        }

        // Mise à jour des champs
        existingSession.setLocation(session.getLocation());
        existingSession.setProgram(session.getProgram());
        existingSession.setStartTime(session.getStartTime());
        existingSession.setEndTime(session.getEndTime());

        return sessionRepo.save(existingSession);
    }
    public void deleteSession(Long sessionId) {
        if (!sessionRepo.existsById(sessionId)) {
            throw new IllegalArgumentException("Session not found with ID: " + sessionId);
        }
        sessionRepo.deleteById(sessionId);
    }
    public Session getOneById(Long id) {
        return sessionRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Session not found with ID: " + id));
    }
    public List<Session> getAll() {
        return sessionRepo.findAll();
    }


}
