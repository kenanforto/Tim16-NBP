package com.hotel.Hotel.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DatabaseConfiguration {

    @Bean
    public Connection jdbcConnection(@Value("${spring.datasource.url}") String url, @Value("${spring.datasource.username}") String username, @Value("${spring.datasource.password}") String password) throws SQLException{
        Connection connection = DriverManager.getConnection(url, username, password);

        if (connection == null)
            throw new SQLException("Can't connect to database.");

        return connection;
    }

}
