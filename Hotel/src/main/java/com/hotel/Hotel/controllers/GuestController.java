package com.hotel.Hotel.controllers;

import com.hotel.Hotel.models.Guest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;  // Ovo je za SQL Date

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/guests")
public class GuestController {

    private final Connection jdbcConnection;

    // Get all guests
    @GetMapping("")
    public ResponseEntity<List<Guest>> getAll() {
        try {
            var resultSet = jdbcConnection.createStatement().executeQuery("SELECT * FROM NBP09.NBP_GUESTS");
            var result = new ArrayList<Guest>();
            while (resultSet.next()) {
                log.info(resultSet.getString("first_name") + " " + resultSet.getString("last_name"));
                Guest guest = new Guest();
                guest.setId(resultSet.getInt("ID"));
                guest.setFirstName(resultSet.getString("FIRST_NAME"));
                guest.setLastName(resultSet.getString("LAST_NAME"));
                guest.setEmail(resultSet.getString("EMAIL"));
                guest.setAddressId(resultSet.getInt("ADDRESS_ID"));
                guest.setPhoneNumber(resultSet.getString("PHONE_NUMBER"));

                // Handle LocalDate conversion from ResultSet
                if (resultSet.getDate("BIRTH_DATE") != null) {
                    guest.setBirthDate(resultSet.getDate("BIRTH_DATE").toLocalDate()); // Convert java.sql.Date to LocalDate
                }

                result.add(guest);
            }
            return ResponseEntity.ok(result);
        } catch (SQLException e) {
            log.error("Error fetching guests", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }


//    // Create a new guest
//    @PostMapping("")
//    public ResponseEntity<Guest> create(@RequestBody Guest guest) {
//        String query = "INSERT INTO NBP09.NBP_GUESTS (ID, FIRST_NAME, LAST_NAME, EMAIL, ADDRESS_ID, PHONE_NUMBER, BIRTH_DATE) VALUES (?, ?, ?, ?, ?, ?, ?)";
//        try (PreparedStatement statement = jdbcConnection.prepareStatement(query)) {
//            statement.setInt(1, guest.getId());
//            statement.setString(1, guest.getFirstName());
//            statement.setString(2, guest.getLastName());
//            statement.setString(3, guest.getEmail());
//            statement.setInt(4, guest.getAddressId());
//            statement.setString(5, guest.getPhoneNumber());
//
//            // Convert LocalDate to java.sql.Date for the BIRTH_DATE column
//            if (guest.getBirthDate() != null) {
//                statement.setDate(6, Date.valueOf(guest.getBirthDate()));  // Convert LocalDate to sql.Date
//            } else {
//                statement.setDate(6, null);  // In case birthDate is null
//            }
//
//            int rowsAffected = statement.executeUpdate();
//            if (rowsAffected > 0) {
//                log.info("Guest created successfully: {}", guest);
//                return ResponseEntity.status(HttpStatus.CREATED).body(guest);  // Return the created guest
//            } else {
//                log.error("Failed to create guest");
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();  // Return a bad request if no rows were affected
//            }
//        } catch (SQLException e) {
//            log.error("Error creating guest", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // Return server error if an exception occurs
//        }
//    }


}
