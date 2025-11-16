package com.anjaanvivek.artistwebsite.controller;

import com.anjaanvivek.artistwebsite.model.User;
import com.anjaanvivek.artistwebsite.service.UserService;
import lombok.Data;   // Lombok
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody UserSignupRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());
        user.setCountry(request.getCountry());
        User savedUser = userService.signup(user, request.getPassword());
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
    	System.out.print("reached hereeeee");
    	String token = userService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}

// --- DTOs ---

@Data
@NoArgsConstructor
@AllArgsConstructor
class UserSignupRequest {
    private String name;
    private String email;
    private String mobile;
    private String country;
    private String password;
    
	public String getName() {
		return name;
	}

	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	public String getCountry() {
		// TODO Auto-generated method stub
		return country;
	}

	public String getMobile() {
		// TODO Auto-generated method stub
		return mobile;
	}

	public String getEmail() {
		// TODO Auto-generated method stub
		return email;
	}
	
	
}

@Data
class LoginRequest {
    private String email;
    private String password;
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
}

class AuthResponse {
    private String token;
    public AuthResponse(String token) { this.token = token; }
    public String getToken() { return token; }
}
