package com.hotel.Hotel.security;


import com.hotel.Hotel.common.dto.UserVM;
import com.hotel.Hotel.common.request.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class AuthenticationController {


    private final AuthenticationService authenticationService;

    @GetMapping(path = "/me")
    public String me(@AuthenticationPrincipal UserDetails userDetails)
    {
        System.out.println(
                userDetails.getAuthorities());
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
    public String authenticateUser(@RequestBody AuthenticationRequest request)
    {
        return authenticationService.authenticate(request);
    }
}
