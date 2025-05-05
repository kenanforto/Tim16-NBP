package com.hotel.Hotel.controllers;

import com.hotel.Hotel.models.Booking;
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
@RequestMapping("/api/bookings")
public class BookingController {
    private final Connection jdbcConnection;

    @GetMapping("")
    public ResponseEntity<List<Booking>> getAll() {
        try {
            var resultSet = jdbcConnection.createStatement().executeQuery("SELECT * FROM NBP09.NBP_BOOKINGS");
            var result = new ArrayList<Booking>();
            while (resultSet.next()) {
                Booking booking = new Booking();
                booking.setId(resultSet.getInt("ID"));
                booking.setGuestId(resultSet.getInt("GUEST_ID"));
                booking.setReservationAgentId(resultSet.getInt("RESERVATION_AGENT_ID"));
                booking.setDateFrom(resultSet.getTimestamp("DATE_FROM").toInstant());
                booking.setDateTo(resultSet.getTimestamp("DATE_TO").toInstant());
                booking.setBookingStatusId(resultSet.getInt("BOOKING_STATUS_ID"));
                result.add(booking);
            }
            return ResponseEntity.ok(result);
        } catch (SQLException e) {
            log.error("Error fetching booking", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    @PostMapping()
    public ResponseEntity<Booking> createBooking(Booking booking) {
        try {
            var maxIdStatement = jdbcConnection.prepareStatement("SELECT COALESCE(MAX(ID), 0) + 1 AS NEXT_ID FROM NBP09.NBP_BOOKINGS");
            var resultSet = maxIdStatement.executeQuery();
            int nextId = 1; // Default to 1 if the table is empty
            if (resultSet.next()) {
                nextId = resultSet.getInt("NEXT_ID");
            }

            var statement = jdbcConnection.prepareStatement("INSERT INTO NBP09.NBP_BOOKINGS(ID, GUEST_ID, RESERVATION_AGENT_ID, DATE_FROM, DATE_TO, BOOKING_STATUS_ID) VALUES (?, ?, ?, ?, ?, ?)", new String[]{"ID"});
            statement.setInt(1, nextId);
            statement.setInt(2, booking.getGuestId());
            statement.setInt(3, booking.getReservationAgentId());
            statement.setObject(4, booking.getDateFrom());
            statement.setObject(5, booking.getDateTo());
            statement.setInt(6, booking.getBookingStatusId());
            statement.executeUpdate();
            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                booking.setId(generatedKeys.getInt(1));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(booking);
        } catch (SQLException e) {
            log.error("Error creating booking", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Integer id, @RequestBody Booking booking) {
        try {
            var statement = jdbcConnection.prepareStatement("UPDATE NBP09.NBP_BOOKINGS SET GUEST_ID = ?, RESERVATION_AGENT_ID = ?, DATE_FROM = ?, DATE_TO = ?, BOOKING_STATUS_ID = ? WHERE ID = ?");
            statement.setInt(1, booking.getGuestId());
            statement.setInt(2, booking.getReservationAgentId());
            statement.setObject(3, booking.getDateFrom());
            statement.setObject(4, booking.getDateTo());
            statement.setInt(5, booking.getBookingStatusId());
            statement.setInt(6, id);
            statement.executeUpdate();
            return ResponseEntity.ok(booking);
        } catch (SQLException e) {
            log.error("Error updating booking", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Integer id) {
        try {
            var statement = jdbcConnection.prepareStatement("DELETE FROM NBP09.NBP_BOOKINGS WHERE ID = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            return ResponseEntity.noContent().build();
        } catch (SQLException e) {
            log.error("Error deleting booking", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Booking>> filterBookings(
            @RequestParam(required = false) Integer guestId,
            @RequestParam(required = false) Integer reservationAgentId,
            @RequestParam(required = false) Integer bookingStatusId,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo
    ) {
        try {
            StringBuilder query = new StringBuilder("SELECT * FROM NBP09.NBP_BOOKINGS WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (guestId != null) {
                query.append(" AND GUEST_ID = ?");
                params.add(guestId);
            }
            if (reservationAgentId != null) {
                query.append(" AND RESERVATION_AGENT_ID = ?");
                params.add(reservationAgentId);
            }
            if (bookingStatusId != null) {
                query.append(" AND BOOKING_STATUS_ID = ?");
                params.add(bookingStatusId);
            }
            if (dateFrom != null) {
                query.append(" AND DATE_FROM >= ?");
                params.add(java.sql.Timestamp.valueOf(dateFrom + " 00:00:00"));
            }
            if (dateTo != null) {
                query.append(" AND DATE_TO <= ?");
                params.add(java.sql.Timestamp.valueOf(dateTo + " 23:59:59"));
            }

            var statement = jdbcConnection.prepareStatement(query.toString());
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }

            var resultSet = statement.executeQuery();
            var bookings = new ArrayList<Booking>();
            while (resultSet.next()) {
                Booking booking = new Booking();
                booking.setId(resultSet.getInt("ID"));
                booking.setGuestId(resultSet.getInt("GUEST_ID"));
                booking.setReservationAgentId(resultSet.getInt("RESERVATION_AGENT_ID"));
                booking.setDateFrom(resultSet.getTimestamp("DATE_FROM").toInstant());
                booking.setDateTo(resultSet.getTimestamp("DATE_TO").toInstant());
                booking.setBookingStatusId(resultSet.getInt("BOOKING_STATUS_ID"));
                bookings.add(booking);
            }

            return ResponseEntity.ok(bookings);
        } catch (SQLException e) {
            log.error("Error filtering bookings", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

}
