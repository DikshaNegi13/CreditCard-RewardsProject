package UserService.user_service.Service;


import UserService.user_service.DTO.User;
import UserService.user_service.UserRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class userService {
    @Autowired
    private UserRepository repo;
    @Autowired
    private PasswordEncoder encoder;

    public User registerUser(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("USER");
        return repo.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return repo.findByusername(username);
    }


}
