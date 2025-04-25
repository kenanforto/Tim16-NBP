package com.hotel.Hotel.controllers;

import com.hotel.Hotel.models.RoomBooked;
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
@RequestMapping("api/room-booked")
public class RoomBookedController {

    private final Connection jdbcConnection;

    @GetMapping("")
    public ResponseEntity<List<RoomBooked>> getAll() {
        try {
            var resultSet = jdbcConnection.createStatement().executeQuery("SELECT * FROM NBP09.NBP_ROOM_BOOKED");
            var result = new ArrayList<RoomBooked>();
            while (resultSet.next()) {
                log.info("getRoomBooked {}", resultSet.getString("description"));
                RoomBooked roomBooked = new RoomBooked();
                roomBooked.setId(resultSet.getInt("ID"));
                roomBooked.setBookingId(resultSet.getInt("BOOKING_ID"));
                roomBooked.setRoomId(resultSet.getInt("ROOM_ID"));
                result.add(roomBooked);
            }
            return ResponseEntity.ok(result);
        } catch (SQLException e) {
            log.error("Error fetching room booked", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    @PostMapping()
    public ResponseEntity<RoomBooked> createRoomBooked(RoomBooked roomBooked) {
        try {
            var maxIdStatement = jdbcConnection.prepareStatement("SELECT COALESCE(MAX(ID), 0) + 1 AS NEXT_ID FROM NBP09.NBP_ROOM_BOOKED");
            var resultSet = maxIdStatement.executeQuery();
            int nextId = 1; // Default to 1 if the table is empty
            if (resultSet.next()) {
                nextId = resultSet.getInt("NEXT_ID");
            }

            var statement = jdbcConnection.prepareStatement("INSERT INTO NBP09.NBP_ROOM_BOOKED (ID, BOOKING_ID, ROOM_ID) VALUES (?, ?, ?)", new String[]{"ID"});
            statement.setInt(1, nextId);
            statement.setInt(2, roomBooked.getBookingId());
            statement.setInt(3, roomBooked.getRoomId());
            statement.executeUpdate();
            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                roomBooked.setId(generatedKeys.getInt(1));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(roomBooked);
        } catch (SQLException e) {
            log.error("Error creating room booked", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomBooked> updateRoomBooked(@PathVariable Integer id, @RequestBody RoomBooked roomBooked) {
        try {
            var statement = jdbcConnection.prepareStatement("UPDATE NBP09.NBP_ROOM_BOOKED SET BOOKING_ID = ?, ROOM_ID = ? WHERE ID = ?");
            statement.setInt(1, roomBooked.getBookingId());
            statement.setInt(2, roomBooked.getRoomId());
            statement.setInt(3, id);
            statement.executeUpdate();
            return ResponseEntity.ok(roomBooked);
        } catch (SQLException e) {
            log.error("Error updating room booked", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomBooked(@PathVariable Integer id) {
        try {
            var statement = jdbcConnection.prepareStatement("DELETE FROM NBP09.NBP_ROOM_BOOKED WHERE ID = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            return ResponseEntity.noContent().build();
        } catch (SQLException e) {
            log.error("Error deleting room booked", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
