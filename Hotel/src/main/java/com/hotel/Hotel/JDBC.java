package com.hotel.Hotel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class JDBC {

    private static final String URL = "";
    private static final String USERNAME = "";
    private static final String PASSWORD = "";

    public Connection getConnection() throws SQLException{
        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        if (connection == null)
            throw new SQLException("Can't connect to database.");

        return connection;
    }
}
