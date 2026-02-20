package UserService.user_service.Controller;
import UserService.user_service.Authentication.JwtUtil;
import UserService.user_service.DTO.AuthResponse;
import UserService.user_service.DTO.User;
import UserService.user_service.Service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/auth")
public class userController
{
    @Autowired
    private userService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public userController(userService userService,JwtUtil jwtUtil, PasswordEncoder passwordEncoder )
    { this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;

    }

    @PostMapping("/register")
    public User register(@RequestBody User user){
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody User userRequest){
        User user = userService.findByUsername(userRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(userRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        if(!jwtUtil.validateToken(token,user.getUsername())){return new AuthResponse("Token Expired... Please generate new");}
        return new AuthResponse(token);
    }




}
