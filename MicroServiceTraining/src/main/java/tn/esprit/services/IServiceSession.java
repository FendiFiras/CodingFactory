package tn.esprit.services;

import tn.esprit.entities.Session;

import java.util.List;

public interface IServiceSession {

    Session addSession(Session session, Long courseId);
    Session updateSession(Session session);
    void deleteSession(Long sessionId);
    Session getOneById(Long id);
    List<Session> getAll();

    List<Session> getSessionsByTraining(Long trainingId);
}
