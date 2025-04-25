package com.hotel.Hotel.controllers;

import com.hotel.Hotel.models.Payments;
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
@RequestMapping("/api/payments")
public class PaymentsController {

    private final Connection jdbcConnection;

    @GetMapping("")
    public ResponseEntity<List<Payments>> getAll() {
        try {
            var resultSet = jdbcConnection.createStatement().executeQuery("SELECT * FROM NBP09.NBP_PAYMENTS");
            var result = new ArrayList<Payments>();
            while (resultSet.next()) {
                log.info("getPayments {}", resultSet.getInt("payment"));
                Payments payment = new Payments();
                payment.setId(resultSet.getInt("ID"));
                payment.setBookingId(resultSet.getInt("BOOKING_ID"));
                payment.setDate(resultSet.getTimestamp("PAYMENT_DATE").toInstant());
                payment.setPayment(resultSet.getInt("PAYMENT"));
                payment.setPaymentTypeId(resultSet.getInt("PAYMENT_TYPE_ID"));
                payment.setPaymentStatusId(resultSet.getInt("PAYMENT_STATUS_ID"));
                result.add(payment);
            }
            return ResponseEntity.ok(result);
        } catch (SQLException e) {
            log.error("Error fetching payment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    @PostMapping()
    public ResponseEntity<Payments> createPayment(Payments payment) {
        try {
            var maxIdStatement = jdbcConnection.prepareStatement("SELECT COALESCE(MAX(ID), 0) + 1 AS NEXT_ID FROM NBP09.NBP_PAYMENTS");
            var resultSet = maxIdStatement.executeQuery();
            int nextId = 1; // Default to 1 if the table is empty
            if (resultSet.next()) {
                nextId = resultSet.getInt("NEXT_ID");
            }

            var statement = jdbcConnection.prepareStatement("INSERT INTO NBP09.NBP_PAYMENTS (ID, BOOKING_ID, PAYMENT_DATE, PAYMENT, PAYMENT_TYPE_ID, PAYMENT_STATUS_ID) VALUES (?, ?, ?, ?, ?, ?)", new String[]{"ID"});
            statement.setInt(1, nextId);
            statement.setInt(2, payment.getBookingId());
            statement.setObject(3, payment.getDate());
            statement.setInt(4, payment.getPayment());
            statement.setInt(5, payment.getPaymentTypeId());
            statement.setInt(6, payment.getPaymentStatusId());
            statement.executeUpdate();
            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                payment.setId(generatedKeys.getInt(1));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(payment);
        } catch (SQLException e) {
            log.error("Error creating payment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payments> updatePayment(@PathVariable Integer id, @RequestBody Payments payment) {
        try {
            var statement = jdbcConnection.prepareStatement("UPDATE NBP09.NBP_PAYMENTS SET BOOKING_ID = ?, PAYMENT_DATE = ?, PAYMENT = ?, PAYMENT_TYPE_ID = ?, PAYMENT_STATUS_ID = ? WHERE ID = ?");
            statement.setInt(1, payment.getBookingId());
            statement.setObject(2, payment.getDate());
            statement.setInt(3, payment.getPayment());
            statement.setInt(4, payment.getPaymentTypeId());
            statement.setInt(5, payment.getPaymentStatusId());
            statement.setInt(6, id);
            statement.executeUpdate();
            return ResponseEntity.ok(payment);
        } catch (SQLException e) {
            log.error("Error updating payment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Integer id) {
        try {
            var statement = jdbcConnection.prepareStatement("DELETE FROM NBP09.NBP_PAYMENTS WHERE ID = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            return ResponseEntity.noContent().build();
        } catch (SQLException e) {
            log.error("Error deleting payment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
