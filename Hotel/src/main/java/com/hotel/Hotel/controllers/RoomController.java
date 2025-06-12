package com.hotel.Hotel.controllers;

import com.hotel.Hotel.common.RoomResponse;
import com.hotel.Hotel.common.dto.PageResponse;
import com.hotel.Hotel.common.request.RoomRequest;
import com.hotel.Hotel.models.Room;
import com.hotel.Hotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
public class RoomController {

    private final Connection jdbcConnection;
    private final RoomService roomService;

    @GetMapping()
    public ResponseEntity<PageResponse<RoomResponse>> getPage(@RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "20") Integer size) {
        log.info("getPage {} {}", page, size);
        var roomPage = roomService.getRoomPage(page, size);
        var roomStatuses = roomService.getAllRoomStatuses();
        var roomTypes = roomService.getAllRoomTypes();
        var response = new PageResponse<RoomResponse>();
        response.setElements(new ArrayList<>());
        for (var room : roomPage) {
            var roomResponse = new RoomResponse();
            roomResponse.setId(room.getId());
            roomResponse.setType(roomTypes.stream().filter(rt -> rt.getId().equals(room.getRoomTypeId())).findFirst().orElse(null));
            roomResponse.setStatus(roomStatuses.stream().filter(rt -> rt.getId().equals(room.getRoomStatusId())).findFirst().orElse(null));
            roomResponse.setFloor(room.getFloor());
            roomResponse.setPrice(room.getPrice());
            roomResponse.setDescription(room.getDescription());
            response.getElements().add(roomResponse);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<Room> createRoom(@RequestBody RoomRequest roomRequest) {
        try {
            var maxIdStatement = jdbcConnection.prepareStatement("SELECT COALESCE(MAX(ID), 0) + 1 AS NEXT_ID FROM NBP09.NBP_ROOMS");
            var resultSet = maxIdStatement.executeQuery();
            int nextId = 1; // Default to 1 if the table is empty
            if (resultSet.next()) {
                nextId = resultSet.getInt("NEXT_ID");
            }

            var statement = jdbcConnection.prepareStatement("INSERT INTO NBP09.NBP_ROOMS (ROOM_TYPE_ID, ROOM_STATUS_ID, FLOOR, PRICE, DESCRIPTION, ID) VALUES (?, ?, ?, ?, ?, ?)", new String[]{"ID"});
            statement.setInt(1, roomRequest.getRoomTypeId());
            statement.setInt(2, roomRequest.getRoomStatusId());
            statement.setInt(3, roomRequest.getFloor());
            statement.setInt(4, roomRequest.getPrice());
            statement.setString(5, roomRequest.getDescription());
            statement.setInt(6, nextId);
            statement.executeUpdate();

            var room = new Room();
            room.setId(nextId);
            room.setRoomTypeId(roomRequest.getRoomTypeId());
            room.setRoomStatusId(roomRequest.getRoomStatusId());
            room.setFloor(roomRequest.getFloor());
            room.setPrice(roomRequest.getPrice());
            room.setDescription(roomRequest.getDescription());

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
    public ResponseEntity<Room> updateRoom(@PathVariable Integer id, @RequestBody RoomRequest roomRequest) {
        try {
            var statement = jdbcConnection.prepareStatement("UPDATE NBP09.NBP_ROOMS SET ROOM_TYPE_ID = ?, ROOM_STATUS_ID = ?, FLOOR = ?, PRICE = ?, DESCRIPTION = ? WHERE ID = ?");
            statement.setInt(1, roomRequest.getRoomTypeId());
            statement.setInt(2, roomRequest.getRoomStatusId());
            statement.setInt(3, roomRequest.getFloor());
            statement.setInt(4, roomRequest.getPrice());
            statement.setString(5, roomRequest.getDescription());
            statement.setInt(6, id);
            statement.executeUpdate();

            var room = new Room();
            room.setId(id);
            room.setRoomTypeId(roomRequest.getRoomTypeId());
            room.setRoomStatusId(roomRequest.getRoomStatusId());
            room.setFloor(roomRequest.getFloor());
            room.setPrice(roomRequest.getPrice());
            room.setDescription(roomRequest.getDescription());

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
