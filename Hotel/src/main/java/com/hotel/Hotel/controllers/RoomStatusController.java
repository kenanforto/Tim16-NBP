package com.hotel.Hotel.controllers;

import com.hotel.Hotel.common.dto.PageResponse;
import com.hotel.Hotel.models.RoomStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room-statuses")
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
                roomStatus.setId(resultSet.getInt("ROOM_STATUS_ID"));
                roomStatus.setDescription(resultSet.getString("DESCRIPTION"));
                result.add(roomStatus);
            }
            return ResponseEntity.ok(result);
        } catch (SQLException e) {
            log.error("Error fetching room statuses", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }
}
