package com.hotel.Hotel.controllers;

import com.hotel.Hotel.common.dto.PageResponse;
import com.hotel.Hotel.models.Address;
import com.hotel.Hotel.models.Room;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {

    private final Connection jdbcConnection;

    @GetMapping
    public ResponseEntity<PageResponse<Room>> getPage(@RequestParam Integer page, @RequestParam Integer size) {
        log.info("getRoomPage {} {}", page, size);
        return ResponseEntity.ok(
                new PageResponse<>(1, List.of(
                        Room.builder()
                                .roomTypeId(1)
                                .description("Room 1")
                                .floor(1)
                                .roomStatusId(1)
                                .id(1)
                                .build()
                ))
        );
    }
}
