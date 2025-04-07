package com.hotel.Hotel.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomBooked {

    private Integer id;
    private Integer bookingId;
    private Integer roomId;

}
