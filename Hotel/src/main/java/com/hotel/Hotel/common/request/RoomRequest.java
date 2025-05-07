package com.hotel.Hotel.common.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomRequest {

    private Integer roomTypeId;
    private Integer roomStatusId;
    private Integer floor;
    private Integer price;
    private String description;

}
