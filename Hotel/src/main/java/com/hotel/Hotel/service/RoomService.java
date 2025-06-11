package com.hotel.Hotel.service;

import com.hotel.Hotel.models.Room;
import com.hotel.Hotel.models.RoomStatus;
import com.hotel.Hotel.models.RoomType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    private final Connection jdbcConnection;

    public List<Room> getRoomPage(int page, int size) {
        try {
            var statement = jdbcConnection.prepareStatement("SELECT * FROM NBP09.NBP_ROOMS OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
            statement.setInt(1, page * size);
            statement.setInt(2, size);
            var resultSet = statement.executeQuery();
            List<Room> rooms = new ArrayList<>();
            while (resultSet.next()) {
                Room room = new Room();
                room.setId(resultSet.getInt("ID"));
                room.setRoomStatusId(resultSet.getInt("ROOM_STATUS_ID"));
                room.setRoomTypeId(resultSet.getInt("ROOM_TYPE_ID"));
                room.setFloor(resultSet.getInt("FLOOR"));
                room.setPrice(resultSet.getInt("PRICE"));
                room.setDescription(resultSet.getString("DESCRIPTION"));
                rooms.add(room);
            }
            return rooms;
        } catch (SQLException e) {
            log.error("Error fetching rooms", e);
            return Collections.emptyList();
        }
    }

    public List<RoomType> getAllRoomTypes() {
        try {
            var resultSet = jdbcConnection.createStatement().executeQuery("SELECT * FROM NBP09.NBP_ROOM_TYPE");
            List<RoomType> roomTypes = new ArrayList<>();
            while (resultSet.next()) {
                RoomType roomType = new RoomType();
                roomType.setId(resultSet.getInt("ID"));
                roomType.setDescription(resultSet.getString("DESCRIPTION"));
                roomTypes.add(roomType);
            }
            return roomTypes;
        } catch (SQLException e) {
            log.error("Error fetching room types", e);
            return Collections.emptyList();
        }
    }

    public List<RoomStatus> getAllRoomStatuses() {
        try {
            var resultSet = jdbcConnection.createStatement().executeQuery("SELECT * FROM NBP09.NBP_ROOM_STATUS");
            List<RoomStatus> roomStatuses = new ArrayList<>();
            while (resultSet.next()) {
                RoomStatus roomStatus = new RoomStatus();
                roomStatus.setId(resultSet.getInt("ID"));
                roomStatus.setDescription(resultSet.getString("DESCRIPTION"));
                roomStatuses.add(roomStatus);
            }
            return roomStatuses;
        } catch (SQLException e) {
            log.error("Error fetching room statuses", e);
            return Collections.emptyList();
        }
    }

}
