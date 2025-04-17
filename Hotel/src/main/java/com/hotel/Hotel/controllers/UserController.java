package com.hotel.Hotel.controllers;

//import com.hotel.Hotel.common.dto.UserVM;
import com.hotel.Hotel.models.RoomStatus;
import com.hotel.Hotel.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
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
            var resultSet = jdbcConnection.createStatement().executeQuery("SELECT * FROM NBP.NBP_USER");
            var result = new ArrayList<User>();
            while (resultSet.next()) {
                var user = new User();
                user.setId(resultSet.getInt("ID"));
                user.setFirstName(resultSet.getString("FIRST_NAME"));
                user.setLastName(resultSet.getString("LAST_NAME"));
                user.setEmail(resultSet.getString("EMAIL"));
                user.setUsername(resultSet.getString("USERNAME"));
                user.setRoleId(resultSet.getInt("ROLE_ID"));
                user.setAddressId(resultSet.getInt("ADDRESS_ID"));
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
        try {
            var saveUser = new User(
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getUsername(),
                    user.getPhoneNumber(),
                    user.getBirthDate(),
                    user.getAddressId(),
                    user.getRoleId(),
                    user.getDeleted()
            );
            System.out.println(saveUser);
            var prepareStatement = jdbcConnection.prepareStatement("INSERT INTO NBP.NBP_USER (ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, USERNAME, PHONE_NUMBER, BIRTH_DATE, ADDRESS_ID, ROLE_ID, DELETED) VALUES(nbp.nbp_user_id_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, '')");
            prepareStatement.setString(1,saveUser.getFirstName());
            prepareStatement.setString(2,saveUser.getLastName());
            prepareStatement.setString(3,saveUser.getEmail());
            prepareStatement.setString(4,saveUser.getPassword());
            prepareStatement.setString(5,saveUser.getUsername());
            prepareStatement.setString(6,saveUser.getPhoneNumber());
            prepareStatement.setDate(7,saveUser.getBirthDate());
            prepareStatement.setInt(8,saveUser.getAddressId());
            prepareStatement.setInt(9,saveUser.getRoleId());
            var resultSet=prepareStatement.executeQuery();
            return ResponseEntity.ok(saveUser);
        } catch (SQLException e) {
            log.error("Error fetching users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new User());
        }
    }
}
