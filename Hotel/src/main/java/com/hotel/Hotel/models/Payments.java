package com.hotel.Hotel.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payments {

    private Integer id;
    private Integer bookingId;
    private Instant date;
    private Integer payment;
    private Integer paymentTypeId;
    private Integer paymentStatusId;

}
