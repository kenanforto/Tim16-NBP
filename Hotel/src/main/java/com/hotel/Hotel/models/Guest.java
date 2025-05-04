package com.hotel.Hotel.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Guest {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer addressId;
    private String phoneNumber;
    private Date birthDate;

}
