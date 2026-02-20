package UserService.user_service.DTO;


import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
