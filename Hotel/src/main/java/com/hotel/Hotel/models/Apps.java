package com.hotel.Hotel.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Apps {
    private Integer id;
    private String appId;
    private Integer managerId;
    private Timestamp expiryDate;
    private Integer roleId;
}
