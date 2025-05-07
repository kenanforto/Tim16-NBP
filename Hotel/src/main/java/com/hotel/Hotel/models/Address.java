package com.hotel.Hotel.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    private Integer id;
    private String street;
    private String city;
    private String country;
    private String zipCode;

    public Address(String street, String city, String country, String zipCode) {
        this.street=street;
        this.city=city;
        this.country=country;
        this.zipCode=zipCode;
    }
}
