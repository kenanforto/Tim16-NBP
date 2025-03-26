package com.hotel.Hotel.models;

import java.sql.Timestamp;

public class Bookings {
    private Integer id;
    private Integer guestId;
    private Integer reservationAgentId;
    private Timestamp dateFrom;
    private Timestamp dateTo;
    private Integer bookingStatusId;
}
