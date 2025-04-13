package tn.esprit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.entities.QuizAnswer;
import tn.esprit.entities.Session;
import tn.esprit.services.IServiceSession;
import tn.esprit.services.IServiceTraining;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/Session")
public class SessionController {


    @Autowired
    private IServiceSession serviceSession ; // Injection automatique du service


    @PostMapping("/add_Session/{courseId}")
    public Session addSession(@RequestBody Session session, @PathVariable Long courseId) {
        return serviceSession.addSession(session, courseId);
    }

    @PutMapping("/update_Session")
    public Session updateSession(@RequestBody Session session) {
        return serviceSession.updateSession(session);
    }
    @DeleteMapping("/deleteSession/{id}")
    public void deleteSession(@PathVariable Long id) {
        serviceSession.deleteSession(id);
    }
    @GetMapping("/getSession/{id}")
    public Session getOneById(@PathVariable Long id) {
        return serviceSession.getOneById(id);


    }
    @GetMapping("/getAllSessions")
    public List<Session> getAll() {
        return serviceSession.getAll();
    }

    // ✅ API pour récupérer les sessions par trainingId
    @GetMapping("/getSessionsByTraining/{trainingId}")
    public ResponseEntity<List<Session>> getSessionsByTraining(@PathVariable Long trainingId) {
        List<Session> sessions = serviceSession.getSessionsByTraining(trainingId);
        return ResponseEntity.ok(sessions);
    }

}
