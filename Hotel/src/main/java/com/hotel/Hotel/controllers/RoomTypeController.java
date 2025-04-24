package com.hotel.Hotel.controllers;

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
                roomType.setId(resultSet.getInt("ROOM_TYPE_ID"));
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
    public ResponseEntity<RoomType> createRoomType(RoomType roomType) {
        try {
            var statement = jdbcConnection.prepareStatement("INSERT INTO NBP09.NBP_ROOM_TYPE (DESCRIPTION) VALUES (?)", new String[]{"ROOM_TYPE_ID"});
            statement.setString(1, roomType.getDescription());
            statement.executeUpdate();
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
    public ResponseEntity<RoomType> updateRoomType(@PathVariable Integer id, @RequestBody RoomType roomType) {
        try {
            var statement = jdbcConnection.prepareStatement("UPDATE NBP09.NBP_ROOM_TYPE SET DESCRIPTION = ? WHERE ROOM_TYPE_ID = ?");
            statement.setString(1, roomType.getDescription());
            statement.setInt(2, id);
            statement.executeUpdate();
            return ResponseEntity.ok(roomType);
        } catch (SQLException e) {
            log.error("Error updating room type", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomType(@PathVariable Integer id) {
        try {
            var statement = jdbcConnection.prepareStatement("DELETE FROM NBP09.NBP_ROOM_TYPE WHERE ROOM_TYPE_ID = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            return ResponseEntity.noContent().build();
        } catch (SQLException e) {
            log.error("Error deleting room type", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
