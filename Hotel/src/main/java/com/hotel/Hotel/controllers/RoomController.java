package com.hotel.Hotel.controllers;

import com.hotel.Hotel.common.dto.PageResponse;
import com.hotel.Hotel.models.Room;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.util.ArrayList;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
public class RoomController {

    private final Connection jdbcConnection;

    @GetMapping
    public ResponseEntity<PageResponse<Room>> getPage(@RequestParam Integer page, @RequestParam Integer size) {
        try {
            var statement = jdbcConnection.prepareStatement("SELECT * FROM NBP09.NBP_ROOMS OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
            statement.setInt(1, page * size);
            statement.setInt(2, size);
            var resultSet = statement.executeQuery();
            var result = new PageResponse<Room>();
            var elements = new ArrayList<Room>();
            while (resultSet.next()) {
                var room = new Room();
                room.setId(resultSet.getInt("ID"));
                room.setRoomTypeId(resultSet.getInt("ROOM_TYPE_ID"));
                room.setRoomStatusId(resultSet.getInt("ROOM_STATUS_ID"));
                room.setFloor(resultSet.getInt("FLOOR"));
                room.setPrice(resultSet.getInt("PRICE"));
                room.setDescription(resultSet.getString("DESCRIPTION"));
                elements.add(room);
            }
            result.setElements(elements);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error fetching rooms", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping()
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        try {
            var statement = jdbcConnection.prepareStatement("INSERT INTO NBP09.NBP_ROOMS (ROOM_TYPE_ID, ROOM_STATUS_ID, FLOOR, PRICE, DESCRIPTION) VALUES (?, ?, ?, ?, ?)", new String[]{"ID"});
            statement.setInt(1, room.getRoomTypeId());
            statement.setInt(2, room.getRoomStatusId());
            statement.setInt(3, room.getFloor());
            statement.setInt(4, room.getPrice());
            statement.setString(5, room.getDescription());
            statement.executeUpdate();
            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                room.setId(generatedKeys.getInt(1));
                return ResponseEntity.ok(room);
            } else {
                return ResponseEntity.status(500).body(null);
            }
        } catch (Exception e) {
            log.error("Error creating room", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Integer id) {
        try {
            var statement = jdbcConnection.prepareStatement("SELECT * FROM NBP09.NBP_ROOMS WHERE ID = ?");
            statement.setInt(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                var room = new Room();
                room.setId(resultSet.getInt("ID"));
                room.setRoomTypeId(resultSet.getInt("ROOM_TYPE_ID"));
                room.setRoomStatusId(resultSet.getInt("ROOM_STATUS_ID"));
                room.setFloor(resultSet.getInt("FLOOR"));
                room.setPrice(resultSet.getInt("PRICE"));
                room.setDescription(resultSet.getString("DESCRIPTION"));
                return ResponseEntity.ok(room);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error fetching room by ID", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Integer id, @RequestBody Room room) {
        try {
            var statement = jdbcConnection.prepareStatement("UPDATE NBP09.NBP_ROOMS SET ROOM_TYPE_ID = ?, ROOM_STATUS_ID = ?, FLOOR = ?, PRICE = ?, DESCRIPTION = ? WHERE ID = ?");
            statement.setInt(1, room.getRoomTypeId());
            statement.setInt(2, room.getRoomStatusId());
            statement.setInt(3, room.getFloor());
            statement.setInt(4, room.getPrice());
            statement.setString(5, room.getDescription());
            statement.setInt(6, id);
            statement.executeUpdate();
            return ResponseEntity.ok(room);
        } catch (Exception e) {
            log.error("Error updating room", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Integer id) {
        try {
            var statement = jdbcConnection.prepareStatement("DELETE FROM NBP09.NBP_ROOMS WHERE ID = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting room", e);
            return ResponseEntity.status(500).build();
        }
    }

}
