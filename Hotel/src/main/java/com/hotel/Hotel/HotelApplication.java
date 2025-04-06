package com.hotel.Hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
public class HotelApplication {

	public static Connection connection;
	public static JDBC jdbc = new JDBC();

	static {
		try {
			connection = jdbc.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public static void main(String[] args) {
		SpringApplication.run(HotelApplication.class, args);
	}

}
