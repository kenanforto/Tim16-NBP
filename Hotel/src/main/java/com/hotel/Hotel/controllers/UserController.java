package com.hotel.Hotel.controllers;

import com.hotel.Hotel.models.Role;
import com.hotel.Hotel.models.RoomStatus;
import com.hotel.Hotel.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
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
                user.setBirthDate(resultSet.getDate("BIRTH_DATE") != null ? Instant.from(resultSet.getDate("BIRTH_DATE").toLocalDate()) : null);
                user.setAddressId(resultSet.getInt("ADDRESS_ID"));
                user.setRoleId(resultSet.getInt("ROLE_ID"));
                user.setRole(roles.stream().filter(role -> role.getId().equals(user.getRoleId())).findFirst().orElseGet(() -> new Role(0, "Unknown")));
                result.add(user);
            }
            return ResponseEntity.ok(result);
        } catch (SQLException e) {
            log.error("Error fetching users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }
}
