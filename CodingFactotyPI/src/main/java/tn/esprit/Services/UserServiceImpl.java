package tn.esprit.Services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.entities.Role;
import tn.esprit.entities.User;
import tn.esprit.Repository.UserRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class UserServiceImpl implements IUserService {

    private UserRepository userRepository;

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User modifyUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            userRepository.delete(user);
        }
    }



    @Override
    public User retrieveUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findByRole(String role) {
        Role roleEnum = Role.valueOf(role); // Convertir le String en Enum
        return userRepository.findByRole(roleEnum);
    }

}