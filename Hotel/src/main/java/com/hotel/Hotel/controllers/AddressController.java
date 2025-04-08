package com.hotel.Hotel.controllers;

import com.hotel.Hotel.models.Address;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/addresses")
public class AddressController {

    private final Connection jdbcConnection;

    // Get all addresses
    @GetMapping("")
    public ResponseEntity<List<Address>> getAll() {
        try {
            var resultSet = jdbcConnection.createStatement().executeQuery("SELECT * FROM NBP09.NBP_ADDRESS");
            var result = new ArrayList<Address>();
            while (resultSet.next()) {
                log.info(resultSet.getString("street"), resultSet.getString("zip_code"));
                Address address = new Address();
                address.setId(resultSet.getInt("ID"));
                address.setStreet(resultSet.getString("STREET"));
                address.setCity(resultSet.getString("CITY"));
                address.setCountry(resultSet.getString("COUNTRY"));
                address.setZipCode(resultSet.getString("ZIP_CODE"));
                result.add(address);
            }
            return ResponseEntity.ok(result);
        } catch (SQLException e) {
            log.error("Error fetching addresses", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }



}
