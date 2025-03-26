package com.hotel.Hotel.models;

import java.sql.Timestamp;

public class Log {
    private Integer id;
    private String actionName;
    private String tableName;
    private Timestamp dateTime;
    private String dbUser;
}
