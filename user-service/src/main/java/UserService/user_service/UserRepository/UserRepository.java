package UserService.user_service.UserRepository;

import UserService.user_service.DTO.LoginRequest;
import UserService.user_service.DTO.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByusername(String username);
}
