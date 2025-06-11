package com.hotel.Hotel.security;

import com.hotel.Hotel.common.UserResponse;
import com.hotel.Hotel.common.dto.UserVM;
import com.hotel.Hotel.common.request.RegistrationRequest;
import com.hotel.Hotel.service.RoleService;
import com.hotel.Hotel.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {


    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final RoleService roleService;


    @GetMapping(path = "/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("User details: {}", userDetails);
        var user = userService.getUserByEmail(userDetails.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        var role = roleService.getRoleById(user.getRoleId());
        if (role == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found");
        }

        var userResponse = UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .birthDate(user.getBirthDate())
                .role(role)
                .build();

        return ResponseEntity.ok(userResponse);
    }

    @PostMapping(path = "/register")
    public UserVM registerUser(@RequestBody RegistrationRequest request) {
        var user = new UserVM();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setBirthDate(request.getBirthDate());
        user.setAddressId(request.getAddressId());
        user.setRoleId(121);
        return authenticationService.register(user);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        log.info("Login Request: {}", request);
        try {
            var token = authenticationService.authenticate(request);
            return ResponseEntity.status(HttpStatus.OK)
                    .header("api-token", "Bearer " + token)
                    .build();
        } catch (Exception e) {
            log.error("Error during authentication", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new HashMap<String, String>() {{
                        put("error", "Invalid credentials");
                    }});
        }
    }
}
