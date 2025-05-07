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
public class BookingRequest {

    private Integer guestId;
    private Integer reservationAgentId;
    private Instant dateFrom;
    private Instant dateTo;
    private Integer bookingStatusId;
}
