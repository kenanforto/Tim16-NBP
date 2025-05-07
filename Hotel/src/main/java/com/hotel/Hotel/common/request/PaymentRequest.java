package com.hotel.Hotel.common.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    private Integer bookingId;
    private Instant date;
    private Integer payment;
    private Integer paymentTypeId;
    private Integer paymentStatusId;

}
