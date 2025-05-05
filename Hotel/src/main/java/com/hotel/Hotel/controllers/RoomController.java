package com.hotel.Hotel.controllers;

import com.hotel.Hotel.common.dto.PageResponse;
import com.hotel.Hotel.models.Room;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

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
            var maxIdStatement = jdbcConnection.prepareStatement("SELECT COALESCE(MAX(ID), 0) + 1 AS NEXT_ID FROM NBP09.NBP_ROOMS");
            var resultSet = maxIdStatement.executeQuery();
            int nextId = 1; // Default to 1 if the table is empty
            if (resultSet.next()) {
                nextId = resultSet.getInt("NEXT_ID");
            }

            var statement = jdbcConnection.prepareStatement("INSERT INTO NBP09.NBP_ROOMS (ROOM_TYPE_ID, ROOM_STATUS_ID, FLOOR, PRICE, DESCRIPTION, ID) VALUES (?, ?, ?, ?, ?, ?)", new String[]{"ID"});
            statement.setInt(1, room.getRoomTypeId());
            statement.setInt(2, room.getRoomStatusId());
            statement.setInt(3, room.getFloor());
            statement.setInt(4, room.getPrice());
            statement.setString(5, room.getDescription());
            statement.setInt(6, nextId);
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

    @GetMapping("/filter")
    public ResponseEntity<List<Room>> filterRooms(
            @RequestParam(required = false) Integer floor,
            @RequestParam(required = false) Integer price,
            @RequestParam(required = false) Integer roomTypeId,
            @RequestParam(required = false) Integer roomStatusId
    ) {
        try {
            StringBuilder query = new StringBuilder("SELECT * FROM NBP09.NBP_ROOMS WHERE 1=1");
            List<Object> parameters = new ArrayList<>();

            if (floor != null) {
                query.append(" AND FLOOR = ?");
                parameters.add(floor);
            }
            if (price != null) {
                query.append(" AND PRICE = ?");
                parameters.add(price);
            }
            if (roomTypeId != null) {
                query.append(" AND ROOM_TYPE_ID = ?");
                parameters.add(roomTypeId);
            }
            if (roomStatusId != null) {
                query.append(" AND ROOM_STATUS_ID = ?");
                parameters.add(roomStatusId);
            }

            var statement = jdbcConnection.prepareStatement(query.toString());

            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }

            var resultSet = statement.executeQuery();
            var rooms = new ArrayList<Room>();
            while (resultSet.next()) {
                var room = new Room();
                room.setId(resultSet.getInt("ID"));
                room.setRoomTypeId(resultSet.getInt("ROOM_TYPE_ID"));
                room.setRoomStatusId(resultSet.getInt("ROOM_STATUS_ID"));
                room.setFloor(resultSet.getInt("FLOOR"));
                room.setPrice(resultSet.getInt("PRICE"));
                room.setDescription(resultSet.getString("DESCRIPTION"));
                rooms.add(room);
            }
            return ResponseEntity.ok(rooms);
        } catch (Exception e) {
            log.error("Error filtering rooms", e);
            return ResponseEntity.status(500).body(null);
        }
    }


}
