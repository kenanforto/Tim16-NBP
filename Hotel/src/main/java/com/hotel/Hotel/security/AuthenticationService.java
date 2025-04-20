package com.hotel.Hotel.security;

import com.hotel.Hotel.common.dto.UserVM;
import com.hotel.Hotel.controllers.UserController;
import com.hotel.Hotel.models.User;
import com.hotel.Hotel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserController userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    public UserVM register(UserVM user)
    {
        User saveUser=new User(user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                passwordEncoder.encode(user.getPassword()),
                user.getUsername(),
                user.getPhoneNumber(),
                user.getBirthDate(),
                user.getAddressId(),
                user.getRoleId(),
        user.getDeleted());
        saveUser=userRepository.saveUser(saveUser).getBody();
        return new UserVM(saveUser.getFirstName(),saveUser.getLastName(),saveUser.getEmail(),saveUser.getPassword(),
                saveUser.getUsername(),saveUser.getPhoneNumber(), saveUser.getBirthDate(), saveUser.getAddressId(), saveUser.getRoleId(), saveUser.getDeleted());
    }

    public String authenticate(AuthenticationRequest authenticationRequest)
    {
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        UserVM user= (UserVM) authentication.getPrincipal();
        return jwtService.generateToken(user);
    }
}
