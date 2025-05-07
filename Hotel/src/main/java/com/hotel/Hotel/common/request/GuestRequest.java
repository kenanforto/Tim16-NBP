package com.hotel.Hotel.common.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestRequest {

    private String firstName;
    private String lastName;
    private String email;
    private Integer addressId;
    private String phoneNumber;
    private Date birthDate;

}
