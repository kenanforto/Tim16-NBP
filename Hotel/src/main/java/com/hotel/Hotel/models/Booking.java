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
public class Booking {

    private Integer id;
    private Integer guestId;
    private Integer reservationAgentId;
    private Instant dateFrom;
    private Instant dateTo;
    private Integer bookingStatusId;

}
