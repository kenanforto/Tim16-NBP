package com.hotel.Hotel.controllers;

import com.hotel.Hotel.common.request.PaymentTypeRequest;
import com.hotel.Hotel.models.PaymentType;
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
@RequestMapping("/api/payment-type")
public class PaymentTypeController {

    private final Connection jdbcConnection;

    @GetMapping("")
    public ResponseEntity<List<PaymentType>> getAll() {
        try {
            var resultSet = jdbcConnection.createStatement().executeQuery("SELECT * FROM NBP09.NBP_PAYMENT_TYPE");
            var result = new ArrayList<PaymentType>();
            while (resultSet.next()) {
                log.info("getPaymentType {}", resultSet.getString("payment_type"));
                PaymentType paymentType = new PaymentType();
                paymentType.setId(resultSet.getInt("ID"));
                paymentType.setPaymentType(resultSet.getString("PAYMENT_TYPE"));
                result.add(paymentType);
            }
            return ResponseEntity.ok(result);
        } catch (SQLException e) {
            log.error("Error fetching payment type", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    @PostMapping()
    public ResponseEntity<PaymentType> createPaymentType(PaymentTypeRequest paymentTypeRequest) {
        try {
            var maxIdStatement = jdbcConnection.prepareStatement("SELECT COALESCE(MAX(ID), 0) + 1 AS NEXT_ID FROM NBP09.NBP_PAYMENT_TYPE");
            var resultSet = maxIdStatement.executeQuery();
            int nextId = 1; // Default to 1 if the table is empty
            if (resultSet.next()) {
                nextId = resultSet.getInt("NEXT_ID");
            }

            var statement = jdbcConnection.prepareStatement("INSERT INTO NBP09.NBP_PAYMENT_TYPE (ID, PAYMENT_TYPE) VALUES (?, ?)", new String[]{"ID"});
            statement.setInt(1, nextId);
            statement.setString(2, paymentTypeRequest.getPaymentType());
            statement.executeUpdate();

            var paymentType = new PaymentType();
            paymentType.setId(nextId);
            paymentType.setPaymentType(paymentTypeRequest.getPaymentType());

            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                paymentType.setId(generatedKeys.getInt(1));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(paymentType);
        } catch (SQLException e) {
            log.error("Error creating payment type", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentType> updatePaymentType(@PathVariable Integer id, @RequestBody PaymentTypeRequest paymentTypeRequest) {
        try {
            var statement = jdbcConnection.prepareStatement("UPDATE NBP09.NBP_PAYMENT_TYPE SET PAYMENT_TYPE = ? WHERE ID = ?");
            statement.setString(1, paymentTypeRequest.getPaymentType());
            statement.setInt(2, id);
            statement.executeUpdate();

            var paymentType = new PaymentType();
            paymentType.setId(id);
            paymentType.setPaymentType(paymentTypeRequest.getPaymentType());

            return ResponseEntity.ok(paymentType);
        } catch (SQLException e) {
            log.error("Error updating payment type", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentType(@PathVariable Integer id) {
        try {
            var statement = jdbcConnection.prepareStatement("DELETE FROM NBP09.NBP_PAYMENT_TYPE WHERE ID = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            return ResponseEntity.noContent().build();
        } catch (SQLException e) {
            log.error("Error deleting payment type", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
