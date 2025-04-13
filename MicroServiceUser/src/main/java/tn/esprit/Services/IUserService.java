package tn.esprit.Services;

import tn.esprit.entities.User;

import java.util.List;
import java.util.Map;

public interface IUserService {
    User addUser(User user);
    List<User> getAllUsers();
    User modifyUser(Long idUser, User user);
    void deleteUser(Long id);
    User retrieveUser(Long id);
    User findByEmail(String email);
    List<User> findByRole(String role);


}