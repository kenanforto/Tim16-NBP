package com.hotel.Hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootApplication
public class HotelApplication {

	public static Connection connection;
	public static JDBC jdbcOracle = new JDBC();

	static {
		try {
			connection = jdbcOracle.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public static void main(String[] args) {
		SpringApplication.run(HotelApplication.class, args);
	}

}
