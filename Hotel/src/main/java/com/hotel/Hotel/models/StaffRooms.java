package com.hotel.Hotel.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffRooms {

    private Integer id;
    private Integer roomId;
    private Integer staffId;

}
