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
public class Log {

    private Integer id;
    private String actionName;
    private String tableName;
    private Timestamp dateTime;
    private String dbUser;

}
