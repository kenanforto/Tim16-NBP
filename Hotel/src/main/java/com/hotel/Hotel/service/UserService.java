package com.hotel.Hotel.service;

import com.hotel.Hotel.models.Role;
import com.hotel.Hotel.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final Connection jdbcConnection;

    public User getUserByEmail(String email) {
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

            return user;
        } catch (SQLException e) {
            log.error("Error fetching user with email {}", email, e);
            return null;
        }
    }
}
