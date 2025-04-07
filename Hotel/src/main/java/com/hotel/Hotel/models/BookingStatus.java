package com.hotel.Hotel.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingStatus {

    private Integer id;
    private Integer status; //treba prepraviti na enum
    private String description;
    private Integer active; // provjeriti za enum

}
