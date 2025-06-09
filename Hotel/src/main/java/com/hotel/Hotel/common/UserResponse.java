package com.hotel.Hotel.common;

import com.hotel.Hotel.models.Address;
import com.hotel.Hotel.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse implements Serializable {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String phoneNumber;
    private Date birthDate;
    private Address address;
    private Role role;

}
