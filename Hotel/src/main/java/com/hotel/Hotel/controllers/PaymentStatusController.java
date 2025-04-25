package com.hotel.Hotel.controllers;


import com.hotel.Hotel.models.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment-status")
public class PaymentStatusController {

    private final Connection jdbcConnection;

    @GetMapping("")
    public ResponseEntity<List<PaymentStatus>> getAll() {
        try {
            var resultSet = jdbcConnection.createStatement().executeQuery("SELECT * FROM NBP09.NBP_PAYMENT_STATUS");
            var result = new ArrayList<PaymentStatus>();
            while (resultSet.next()) {
                log.info("getPaymentStatus {}", resultSet.getString("status"));
                PaymentStatus paymentStatus = new PaymentStatus();
                paymentStatus.setId(resultSet.getInt("ID"));
                paymentStatus.setStatus(resultSet.getString("STATUS"));
                paymentStatus.setDescription(resultSet.getString("DESCRIPTION"));
                result.add(paymentStatus);
            }
            return ResponseEntity.ok(result);
        } catch (SQLException e) {
            log.error("Error fetching payment status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    @PostMapping()
    public ResponseEntity<PaymentStatus> createPaymentStatus(PaymentStatus paymentStatus) {
        try {
            var maxIdStatement = jdbcConnection.prepareStatement("SELECT COALESCE(MAX(ID), 0) + 1 AS NEXT_ID FROM NBP09.NBP_PAYMENT_STATUS");
            var resultSet = maxIdStatement.executeQuery();
            int nextId = 1; // Default to 1 if the table is empty
            if (resultSet.next()) {
                nextId = resultSet.getInt("NEXT_ID");
            }

            var statement = jdbcConnection.prepareStatement("INSERT INTO NBP09.NBP_PAYMENT_STATUS (ID, STATUS, DESCRIPTION) VALUES (?, ?, ?)", new String[]{"ID"});
            statement.setInt(1, nextId);
            statement.setString(2, paymentStatus.getStatus());
            statement.setString(3, paymentStatus.getDescription());
            statement.executeUpdate();
            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                paymentStatus.setId(generatedKeys.getInt(1));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(paymentStatus);
        } catch (SQLException e) {
            log.error("Error creating payment status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentStatus> updatePaymentStatus(@PathVariable Integer id, @RequestBody PaymentStatus paymentStatus) {
        try {
            var statement = jdbcConnection.prepareStatement("UPDATE NBP09.NBP_PAYMENT_STATUS SET STATUS = ?, DESCRIPTION = ? WHERE ID = ?");
            statement.setString(1, paymentStatus.getStatus());
            statement.setString(2, paymentStatus.getDescription());
            statement.setInt(3, id);
            statement.executeUpdate();
            return ResponseEntity.ok(paymentStatus);
        } catch (SQLException e) {
            log.error("Error updating payment status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentStatus(@PathVariable Integer id) {
        try {
            var statement = jdbcConnection.prepareStatement("DELETE FROM NBP09.NBP_PAYMENT_STATUS WHERE ID = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            return ResponseEntity.noContent().build();
        } catch (SQLException e) {
            log.error("Error deleting payment status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
