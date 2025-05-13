package com.hotel.Hotel.security;


import com.hotel.Hotel.common.dto.UserVM;
import com.hotel.Hotel.common.request.RegistrationRequest;
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

    @GetMapping(path = "/me")
    public String me(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("User details: {}", userDetails);
        return userDetails.getUsername();
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new HashMap<String, String>() {{
                        put("error", "Invalid credentials");
                    }});
        }
    }
}
