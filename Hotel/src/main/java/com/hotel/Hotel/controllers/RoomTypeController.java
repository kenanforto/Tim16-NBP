package com.hotel.Hotel.controllers;

import com.hotel.Hotel.common.request.RoomTypeRequest;
import com.hotel.Hotel.models.RoomStatus;
import com.hotel.Hotel.models.RoomType;
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
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/room-type")
public class RoomTypeController {

    private final Connection jdbcConnection;

    @GetMapping()
    public ResponseEntity<List<RoomType>> getAll() {
        try {
            var resultSet = jdbcConnection.createStatement().executeQuery("SELECT * FROM NBP09.NBP_ROOM_TYPE");
            var result = new ArrayList<RoomType>();
            while (resultSet.next()) {
                var roomType = new RoomType();
                roomType.setId(resultSet.getInt("ID"));
                roomType.setDescription(resultSet.getString("DESCRIPTION"));
                result.add(roomType);
            }
            return ResponseEntity.ok(result);
        } catch (SQLException e) {
            log.error("Error fetching room statuses", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    @PostMapping()
    public ResponseEntity<RoomType> createRoomType(@RequestBody RoomTypeRequest roomTypeRequest) {
        try {
            System.out.println("neki "+ roomTypeRequest);
            var maxIdStatement = jdbcConnection.prepareStatement("SELECT COALESCE(MAX(ID), 0) + 1 AS NEXT_ID FROM NBP09.NBP_ROOM_TYPE");
            var resultSet = maxIdStatement.executeQuery();
            int nextId = 1; // Default to 1 if the table is empty
            if (resultSet.next()) {
                nextId = resultSet.getInt("NEXT_ID");
            }

            var statement = jdbcConnection.prepareStatement("INSERT INTO NBP09.NBP_ROOM_TYPE (DESCRIPTION, ID) VALUES (?, ?)", new String[]{"ID"});
            statement.setString(1, roomTypeRequest.getDescription());
            statement.setInt(2, nextId);
            statement.executeUpdate();
            var roomType = new RoomType();
            roomType.setDescription(roomTypeRequest.getDescription());
            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                roomType.setId(generatedKeys.getInt(1));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(roomType);
        } catch (SQLException e) {
            log.error("Error creating room type", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomType> updateRoomType(@PathVariable Integer id, @RequestBody RoomTypeRequest roomTypeRequest) {
        try {
            var statement = jdbcConnection.prepareStatement("UPDATE NBP09.NBP_ROOM_TYPE SET DESCRIPTION = ? WHERE ID = ?");
            statement.setString(1, roomTypeRequest.getDescription());
            statement.setInt(2, id);
            statement.executeUpdate();
            var roomType = new RoomType();
            roomType.setDescription(roomTypeRequest.getDescription());
            roomType.setId(id);
            return ResponseEntity.ok(roomType);
        } catch (SQLException e) {
            log.error("Error updating room type", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomType(@PathVariable Integer id) {
        try {
            var statement = jdbcConnection.prepareStatement("DELETE FROM NBP09.NBP_ROOM_TYPE WHERE ID = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            return ResponseEntity.noContent().build();
        } catch (SQLException e) {
            log.error("Error deleting room type", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
