package tn.esprit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.entities.Session;
import tn.esprit.services.IServiceSession;
import tn.esprit.services.IServiceTraining;

import java.util.List;

@RestController
@RequestMapping("/Session")
public class SessionController {


    @Autowired
    private IServiceSession serviceSession ; // Injection automatique du service


    @PostMapping("/add_Session")
    public Session addSession(@RequestBody Session session, @RequestParam Long courseId) {
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



}
