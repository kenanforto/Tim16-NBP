package com.hotel.Hotel.controllers;

import com.hotel.Hotel.common.request.BookingStatusRequest;
import com.hotel.Hotel.models.BookingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/booking-status")
public class BookingStatusController {

    private final Connection jdbcConnection;

    @GetMapping("")
    public ResponseEntity<List<BookingStatus>> getAll() {
        try {
            var resultSet = jdbcConnection.createStatement().executeQuery("SELECT * FROM NBP09.NBP_BOOKING_STATUS");
            var result = new ArrayList<BookingStatus>();
            while (resultSet.next()) {
                log.info("getBookingStatus {}", resultSet.getString("status"));
                BookingStatus bookingStatus = new BookingStatus();
                bookingStatus.setId(resultSet.getInt("ID"));
                bookingStatus.setStatus(resultSet.getString("STATUS"));
                bookingStatus.setDescription(resultSet.getString("DESCRIPTION"));
                bookingStatus.setActive(resultSet.getInt("ACTIVE"));
                result.add(bookingStatus);
            }
            return ResponseEntity.ok(result);
        } catch (SQLException e) {
            log.error("Error fetching booking status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    @PostMapping()
    public ResponseEntity<BookingStatus> createBookingStatus(BookingStatusRequest bookingStatusRequest) {
        try {
            var maxIdStatement = jdbcConnection.prepareStatement("SELECT COALESCE(MAX(ID), 0) + 1 AS NEXT_ID FROM NBP09.NBP_BOOKING_STATUS");
            var resultSet = maxIdStatement.executeQuery();
            int nextId = 1; // Default to 1 if the table is empty
            if (resultSet.next()) {
                nextId = resultSet.getInt("NEXT_ID");
            }

            var statement = jdbcConnection.prepareStatement("INSERT INTO NBP09.NBP_BOOKING_STATUS(ID, STATUS, DESCRIPTION, ACTIVE) VALUES (?, ?, ?, ?)", new String[]{"ID"});
            statement.setInt(1, nextId);
            statement.setString(2, bookingStatusRequest.getStatus());
            statement.setString(3, bookingStatusRequest.getDescription());
            statement.setInt(4, bookingStatusRequest.getActive());
            statement.executeUpdate();

            var bookingStatus = new BookingStatus();
            bookingStatus.setId(nextId);
            bookingStatus.setStatus(bookingStatusRequest.getStatus());
            bookingStatus.setDescription(bookingStatusRequest.getDescription());
            bookingStatus.setActive(bookingStatusRequest.getActive());

            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                bookingStatus.setId(generatedKeys.getInt(1));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(bookingStatus);
        } catch (SQLException e) {
            log.error("Error creating booking status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingStatus> updatePayment(@PathVariable Integer id, @RequestBody BookingStatusRequest bookingStatusRequest) {
        try {
            var statement = jdbcConnection.prepareStatement("UPDATE NBP09.NBP_BOOKING_STATUS SET STATUS = ?, DESCRIPTION = ?, ACTIVE = ? WHERE ID = ?");
            statement.setString(1, bookingStatusRequest.getStatus());
            statement.setString(2, bookingStatusRequest.getDescription());
            statement.setInt(3, bookingStatusRequest.getActive());
            statement.setInt(4, id);
            statement.executeUpdate();

            var bookingStatus = new BookingStatus();
            bookingStatus.setId(id);
            bookingStatus.setStatus(bookingStatusRequest.getStatus());
            bookingStatus.setDescription(bookingStatusRequest.getDescription());
            bookingStatus.setActive(bookingStatusRequest.getActive());

            return ResponseEntity.ok(bookingStatus);
        } catch (SQLException e) {
            log.error("Error updating booking status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Integer id) {
        try {
            var statement = jdbcConnection.prepareStatement("DELETE FROM NBP09.NBP_BOOKING_STATUS WHERE ID = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            return ResponseEntity.noContent().build();
        } catch (SQLException e) {
            log.error("Error deleting booking status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
