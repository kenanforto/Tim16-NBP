package com.hotel.Hotel.controllers;

import com.hotel.Hotel.common.dto.MailBody;
import com.hotel.Hotel.common.request.RoomBookedRequest;
import com.hotel.Hotel.models.Role;
import com.hotel.Hotel.models.RoomBooked;
import com.hotel.Hotel.models.User;
import com.hotel.Hotel.security.AuthenticationRequest;
import com.hotel.Hotel.security.JwtService;
import com.hotel.Hotel.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final Connection jdbcConnection;
    private final EmailService emailService;


    private final JwtService jwtService;

    @GetMapping()
    public ResponseEntity<List<User>> getAll() {
        try {
            var statement = jdbcConnection.createStatement();
            var roleResultSet = statement.executeQuery("SELECT * FROM NBP.NBP_ROLE");
            var roles = new ArrayList<Role>();
            while (roleResultSet.next()) {
                var role = new Role();
                role.setId(roleResultSet.getInt("ID"));
                role.setName(roleResultSet.getString("NAME"));
                roles.add(role);
            }

            var resultSet = statement.executeQuery("SELECT * FROM NBP.NBP_USER");
            var result = new ArrayList<User>();
            while (resultSet.next()) {
                var user = new User();
                user.setId(resultSet.getInt("ID"));
                user.setFirstName(resultSet.getString("FIRST_NAME"));
                user.setLastName(resultSet.getString("LAST_NAME"));
                user.setEmail(resultSet.getString("EMAIL"));
                user.setUsername(resultSet.getString("USERNAME"));
                user.setPhoneNumber(resultSet.getString("PHONE_NUMBER"));
                user.setBirthDate(resultSet.getDate("BIRTH_DATE"));
                user.setAddressId(resultSet.getInt("ADDRESS_ID"));
                user.setRoleId(resultSet.getInt("ROLE_ID"));
                result.add(user);
            }
            return ResponseEntity.ok(result);
        } catch (SQLException e) {
            log.error("Error fetching users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    @GetMapping(path="{username}")
    public ResponseEntity<User> getByName(@PathVariable String username) {
        try {
            var resultSet = jdbcConnection.createStatement().executeQuery("SELECT * FROM NBP.NBP_USER WHERE username='" + username + "'");
            System.out.println(resultSet);
            var user = new User();
            while (resultSet.next()) {
                user.setId(resultSet.getInt("ID"));
                user.setFirstName(resultSet.getString("FIRST_NAME"));
                user.setLastName(resultSet.getString("LAST_NAME"));
                user.setEmail(resultSet.getString("EMAIL"));
                user.setUsername(resultSet.getString("USERNAME"));
                user.setPassword(resultSet.getString("PASSWORD"));
                user.setRoleId(resultSet.getInt("ROLE_ID"));
                user.setAddressId(resultSet.getInt("ADDRESS_ID"));
            }

            return ResponseEntity.ok(user);
        } catch (SQLException e) {
            log.error("Error fetching users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new User());
        }
    }

    @GetMapping(path="{email}")
    public ResponseEntity<User> getByEmail(@PathVariable String email) {
        try {
            var resultSet = jdbcConnection.createStatement().executeQuery("SELECT * FROM NBP.NBP_USER WHERE email='" + email + "'");
            System.out.println(resultSet);
            var user = new User();
            while (resultSet.next()) {
                user.setId(resultSet.getInt("ID"));
                user.setFirstName(resultSet.getString("FIRST_NAME"));
                user.setLastName(resultSet.getString("LAST_NAME"));
                user.setEmail(resultSet.getString("EMAIL"));
                user.setUsername(resultSet.getString("USERNAME"));
                user.setPassword(resultSet.getString("PASSWORD"));
                user.setRoleId(resultSet.getInt("ROLE_ID"));
                user.setAddressId(resultSet.getInt("ADDRESS_ID"));
            }

            return ResponseEntity.ok(user);
        } catch (SQLException e) {
            log.error("Error fetching users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new User());
        }
    }

    @PostMapping()
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        Integer userId=1;
        try {
            ResultSet resultSet=jdbcConnection.prepareStatement("SELECT MAX(ID) FROM NBP.NBP_USER").executeQuery();
            if(resultSet.next())
            {
                userId=resultSet.getInt(1)+1;
            }
            System.out.println("userId "+ userId);
        }
        catch (SQLException e) {
            log.error("Error with creating guest", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new User());
        }
        try {
            User saveUser = new User(
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getUsername(),
                    user.getPhoneNumber(),
                    user.getBirthDate(),
                    user.getAddressId(),
                    user.getRoleId()
            );
            System.out.println(saveUser);
            var prepareStatement = jdbcConnection.prepareStatement("INSERT INTO NBP.NBP_USER (ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, USERNAME, PHONE_NUMBER, BIRTH_DATE, ADDRESS_ID, ROLE_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            prepareStatement.setInt(1, userId);
            prepareStatement.setString(2,saveUser.getFirstName());
            prepareStatement.setString(3,saveUser.getLastName());
            prepareStatement.setString(4,saveUser.getEmail());
            prepareStatement.setString(5,saveUser.getPassword());
            prepareStatement.setString(6,saveUser.getUsername());
            prepareStatement.setString(7,saveUser.getPhoneNumber());
            prepareStatement.setDate(8,saveUser.getBirthDate());
            prepareStatement.setInt(9,saveUser.getAddressId());
            prepareStatement.setInt(10,saveUser.getRoleId());
            prepareStatement.executeQuery();
            saveUser.setId(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(saveUser);

        } catch (SQLException e) {
            log.error("Error fetching users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new User());
        }
    }

    @PostMapping("/email-reset-password")
    public ResponseEntity emailResetPassword(@RequestParam String email ){
        User user=getByEmail(email).getBody();
        if (user.equals(new User())) {
            return new ResponseEntity("User with "+email+" email doesn't exist!", HttpStatus.NOT_FOUND);
        }
        String token =jwtService.generateToken(
                User.entityToDto(user)
        );
        String resetLink = "http://localhost:8080/reset-password/confirm?token=" + token;
        try {
            MailBody mailBody = new MailBody(
                    email,
                    "Reset Your Password",
                    "Click the link to reset your password: " + resetLink
            );
            emailService.sendSimpleMessage(mailBody);
        } catch (Exception e) {
            return new ResponseEntity("Email sending failed", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Email is successfully sent");
    }

    @PutMapping("/reset-password")
    public ResponseEntity<User> updateRoomBooked(@RequestBody AuthenticationRequest authenticationRequest) {

        try {
            var user=getByEmail(authenticationRequest.getUsername()).getBody();
            if(user.equals(new User()))
            {
                return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
            }
            var statement = jdbcConnection.prepareStatement("UPDATE NBP09.NBP_USER SET ID=?, FIRST_NAME=?, LAST_NAME=?, EMAIL=?, PASSWORD=?, USERNAME=?, PHONE_NUMBER=?, BIRTH_DATE=?, ADDRESS_ID=?, ROLE_ID=?");
            statement.setInt(1, user.getId());
            statement.setString(3,user.getLastName());
            statement.setString(4,user.getEmail());
            statement.setString(5,user.getPassword());
            statement.setString(6,user.getUsername());
            statement.setString(7,user.getPhoneNumber());
            statement.setDate(8,user.getBirthDate());
            statement.setInt(9,user.getAddressId());
            statement.setInt(10,user.getRoleId());
            statement.executeUpdate();

            return ResponseEntity.ok(user);
        } catch (SQLException e) {
            log.error("Error updating user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
