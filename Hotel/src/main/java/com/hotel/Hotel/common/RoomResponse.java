package com.hotel.Hotel.common;

import com.hotel.Hotel.models.RoomStatus;
import com.hotel.Hotel.models.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponse {

    private Integer id;
    private RoomType type;
    private RoomStatus status;
    private Integer floor;
    private Integer price;
    private String description;

}
