package com.hotel.Hotel.controllers;

import com.hotel.Hotel.models.Guest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                guest.setBirthDate(resultSet.getDate("BIRTH_DATE"));
                result.add(guest);
            }
            return ResponseEntity.ok(result);
        } catch (SQLException e) {
            log.error("Error fetching guests", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Guest> getGuestById(@PathVariable int id) {
        try {
            var resultSet = jdbcConnection.createStatement().executeQuery("SELECT * FROM NBP09.NBP_GUESTS WHERE ID = " + id);

            Guest guest = new Guest();
            if (resultSet.next()) {
                guest.setId(resultSet.getInt("ID"));
                guest.setFirstName(resultSet.getString("FIRST_NAME"));
                guest.setLastName(resultSet.getString("LAST_NAME"));
                guest.setEmail(resultSet.getString("EMAIL"));
                guest.setAddressId(resultSet.getInt("ADDRESS_ID"));
                guest.setPhoneNumber(resultSet.getString("PHONE_NUMBER"));
                guest.setBirthDate(resultSet.getDate("BIRTH_DATE"));
                return ResponseEntity.ok(guest);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

        } catch (SQLException e) {
            log.error("Error fetching guest", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("")
    public ResponseEntity<Guest> create(@RequestBody Guest savedGuest) {
        Integer guestId=1;
        try {
            ResultSet resultSet=jdbcConnection.prepareStatement("SELECT MAX(ID) FROM NBP09.NBP_GUESTS").executeQuery();
            if(resultSet.next())
            {
                guestId=resultSet.getInt(1)+1;
            }
            System.out.println("guestid "+ guestId);
        }
        catch (SQLException e) {
            log.error("Error with creating guest", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Guest());
        }
        System.out.println("nejla "+ savedGuest);
        try {
            PreparedStatement statement = jdbcConnection.prepareStatement(
                    "INSERT INTO NBP09.NBP_GUESTS (ID, FIRST_NAME, LAST_NAME, EMAIL, ADDRESS_ID, PHONE_NUMBER, BIRTH_DATE) VALUES (?, ?, ?, ?, ?, ?, ?)");
            statement.setInt(1, guestId);
            statement.setString(2, savedGuest.getFirstName());
            statement.setString(3, savedGuest.getLastName());
            statement.setString(4, savedGuest.getEmail());
            statement.setInt(5, savedGuest.getAddressId());
            statement.setString(6, savedGuest.getPhoneNumber());
            statement.setDate(7, savedGuest.getBirthDate());

            statement.executeQuery();
            savedGuest.setId(guestId);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedGuest);

        } catch (SQLException e) {
            log.error("Error creating guest", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Guest());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGuestById(@PathVariable Integer id) {
        try {
            PreparedStatement checkStmt = jdbcConnection.prepareStatement("SELECT * FROM NBP09.NBP_GUESTS WHERE ID = ?");
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Guest with ID " + id + " not found.");
            }

            PreparedStatement deleteStmt = jdbcConnection.prepareStatement("DELETE FROM NBP09.NBP_GUESTS WHERE ID = ?");
            deleteStmt.setInt(1, id);
            deleteStmt.executeUpdate();

            return ResponseEntity.ok("Guest with ID " + id + " successfully deleted.");
        } catch (SQLException e) {
            log.error("Error deleting guest", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the guest.");
        }
    }

}
