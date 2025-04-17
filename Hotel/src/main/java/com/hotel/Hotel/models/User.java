package com.hotel.Hotel.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.sql.Date;

@Entity
@Table(name = "NBP.NBP_USER")
@Data
@NoArgsConstructor
public class User {

    @Id
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String username;
    private String phoneNumber;
    private Date birthDate;
    private Integer addressId;
    private Integer roleId;
    private Timestamp deleted;


    public User(String firstName,
                String lastName,
                String email,
                String password,
                String username,
                String phoneNumber,
                Date birthDate,
                Integer addressId,
                Integer roleId,
                Timestamp deleted) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.addressId = addressId;
        this.roleId = roleId;
        this.deleted = deleted;
    }
}
