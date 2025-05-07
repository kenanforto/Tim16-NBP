package com.hotel.Hotel.controllers;

import com.hotel.Hotel.common.request.AddressRequest;
import com.hotel.Hotel.models.Address;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
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
    @GetMapping(path="{id}")
    public ResponseEntity<Address> getById(@PathVariable Integer id) {
        try {
            var resultSet = jdbcConnection.createStatement().executeQuery("SELECT * FROM NBP09.NBP_ADDRESS WHERE id='" + id + "'");
            System.out.println(resultSet);
            var address = new Address();
            while (resultSet.next()) {
                address.setId(resultSet.getInt("ID"));
                address.setStreet(resultSet.getString("STREET"));
                address.setCity(resultSet.getString("CITY"));
                address.setCountry(resultSet.getString("COUNTRY"));
                address.setZipCode(resultSet.getString("ZIP_CODE"));
            }
            System.out.println(address);

            return ResponseEntity.ok(address);
        } catch (SQLException e) {
            log.error("Error fetching address", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Address());
        }
    }

    @PostMapping()
    public ResponseEntity<Address> saveAddress(@RequestBody AddressRequest addressRequest) {
        Address saveAddress;
        Integer addressId=1;
        try {
            ResultSet resultSet=jdbcConnection.prepareStatement("SELECT MAX(ID) FROM NBP09.NBP_ADDRESS").executeQuery();
            if(resultSet.next())
            {
                addressId=resultSet.getInt(1)+1;
            }
        }
        catch (SQLException e) {
            log.error("Error creating address", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Address());
        }
        try {
            saveAddress = new Address(
                    addressId,
                    addressRequest.getStreet(),
                    addressRequest.getCity(),
                    addressRequest.getCountry(),
                    addressRequest.getZipCode()
            );
            var prepareStatement = jdbcConnection.prepareStatement("INSERT INTO NBP09.NBP_ADDRESS (ID, STREET, CITY, COUNTRY, ZIP_CODE) VALUES(?, ?, ?, ?, ?)");
            prepareStatement.setInt(1,saveAddress.getId());
            prepareStatement.setString(2,saveAddress.getStreet());
            prepareStatement.setString(3,saveAddress.getCity());
            prepareStatement.setString(4,saveAddress.getCountry());
            prepareStatement.setString(5,saveAddress.getZipCode());
            var resultSet=prepareStatement.executeQuery();
            return ResponseEntity.ok(saveAddress);
        } catch (SQLException e) {
            log.error("Error fetching address", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Address());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable Integer id, @RequestBody AddressRequest addressRequest) {
        try {
            var updatedAddress=new Address(
                    id,
                    addressRequest.getStreet(),
                    addressRequest.getCity(),
                    addressRequest.getCountry(),
                    addressRequest.getZipCode()
            );
            var statement = jdbcConnection.prepareStatement("UPDATE NBP09.NBP_ADDRESS SET STREET = ?, CITY = ?, COUNTRY = ?, ZIP_CODE = ?");
            statement.setString(1, addressRequest.getStreet());
            statement.setString(2, addressRequest.getCity());
            statement.setObject(3, addressRequest.getCountry());
            statement.setObject(4, addressRequest.getZipCode());
            statement.executeUpdate();
            return ResponseEntity.ok(updatedAddress);
        } catch (SQLException e) {
            log.error("Error updating address", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Address());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Integer id) {
        try {
            var statement = jdbcConnection.prepareStatement("DELETE FROM NBP09.NBP_ADDRESS WHERE ID = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            return ResponseEntity.noContent().build();
        } catch (SQLException e) {
            log.error("Error deleting address", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
