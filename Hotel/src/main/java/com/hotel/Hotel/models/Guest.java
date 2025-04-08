package com.hotel.Hotel.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private LocalDate birthDate;  // Use LocalDate for date without time

}
