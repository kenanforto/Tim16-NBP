package com.hotel.Hotel.controllers;


import com.hotel.Hotel.models.Guest;
import com.hotel.Hotel.models.Role;
import com.hotel.Hotel.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final Connection jdbcConnection;

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
            System.out.println(user);

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
}
