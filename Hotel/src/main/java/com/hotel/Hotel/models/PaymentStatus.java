package com.hotel.Hotel.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentStatus {

    private Integer id;
    private Integer paymentType; //prebaciti u enum

}
