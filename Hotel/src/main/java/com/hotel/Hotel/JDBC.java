package com.hotel.Hotel;

import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC {

    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.username}")
    private String dbUsername;
    @Value("${spring.datasource..passsword}")
    private String dbPassword;

    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

        if (connection == null) {
            throw new SQLException("Database connection failed.");
        }
        return connection;
    }
}
