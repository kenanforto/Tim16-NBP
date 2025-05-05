package com.hotel.Hotel.common.request;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private Date birthDate;
    private Integer addressId;

}
