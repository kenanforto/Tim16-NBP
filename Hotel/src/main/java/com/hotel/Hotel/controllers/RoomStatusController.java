package com.hotel.Hotel.controllers;

import com.hotel.Hotel.common.request.RoomStatusRequest;
import com.hotel.Hotel.models.RoomStatus;
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
@RequestMapping("/api/room-status")
public class RoomStatusController {

    private final Connection jdbcConnection;

    @GetMapping("")
    public ResponseEntity<List<RoomStatus>> getAll() {
        try {
            var resultSet = jdbcConnection.createStatement().executeQuery("SELECT * FROM NBP09.NBP_ROOM_STATUS");
            var result = new ArrayList<RoomStatus>();
            while (resultSet.next()) {
                log.info("getRoomStatus {}", resultSet.getString("description"));
                RoomStatus roomStatus = new RoomStatus();
                roomStatus.setId(resultSet.getInt("ID"));
                roomStatus.setDescription(resultSet.getString("DESCRIPTION"));
                result.add(roomStatus);
            }
            return ResponseEntity.ok(result);
        } catch (SQLException e) {
            log.error("Error fetching room statuses", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    @PostMapping()
    public ResponseEntity<RoomStatus> createRoomStatus(RoomStatusRequest roomStatusRequest) {
        try {
            var maxIdStatement = jdbcConnection.prepareStatement("SELECT COALESCE(MAX(ID), 0) + 1 AS NEXT_ID FROM NBP09.NBP_ROOM_STATUS");
            var resultSet = maxIdStatement.executeQuery();
            int nextId = 1; // Default to 1 if the table is empty
            if (resultSet.next()) {
                nextId = resultSet.getInt("NEXT_ID");
            }

            var statement = jdbcConnection.prepareStatement("INSERT INTO NBP09.NBP_ROOM_STATUS (DESCRIPTION, ID) VALUES (?, ?)", new String[]{"ID"});
            statement.setString(1, roomStatusRequest.getDescription());
            statement.setInt(2, nextId);
            statement.executeUpdate();

            var roomStatus = new RoomStatus();
            roomStatus.setId(nextId);
            roomStatus.setDescription(roomStatusRequest.getDescription());

            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                roomStatus.setId(generatedKeys.getInt(1));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(roomStatus);
        } catch (SQLException e) {
            log.error("Error creating room status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomStatus> updateRoomStatus(@PathVariable Integer id, @RequestBody RoomStatusRequest roomStatusRequest) {
        try {
            var statement = jdbcConnection.prepareStatement("UPDATE NBP09.NBP_ROOM_STATUS SET DESCRIPTION = ? WHERE ID = ?");
            statement.setString(1, roomStatusRequest.getDescription());
            statement.setInt(2, id);
            statement.executeUpdate();

            var roomStatus = new RoomStatus();
            roomStatus.setId(id);
            roomStatus.setDescription(roomStatusRequest.getDescription());

            return ResponseEntity.ok(roomStatus);
        } catch (SQLException e) {
            log.error("Error updating room status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomStatus(@PathVariable Integer id) {
        try {
            var statement = jdbcConnection.prepareStatement("DELETE FROM NBP09.NBP_ROOM_STATUS WHERE ID = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            return ResponseEntity.noContent().build();
        } catch (SQLException e) {
            log.error("Error deleting room status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
