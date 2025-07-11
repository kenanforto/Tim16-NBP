package com.hotel.Hotel.controllers;

import com.hotel.Hotel.common.request.PaymentRequest;
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
    public ResponseEntity<Payments> createPayment(PaymentRequest paymentRequest) {
        try {
            var maxIdStatement = jdbcConnection.prepareStatement("SELECT COALESCE(MAX(ID), 0) + 1 AS NEXT_ID FROM NBP09.NBP_PAYMENTS");
            var resultSet = maxIdStatement.executeQuery();
            int nextId = 1; // Default to 1 if the table is empty
            if (resultSet.next()) {
                nextId = resultSet.getInt("NEXT_ID");
            }

            var statement = jdbcConnection.prepareStatement("INSERT INTO NBP09.NBP_PAYMENTS (ID, BOOKING_ID, PAYMENT_DATE, PAYMENT, PAYMENT_TYPE_ID, PAYMENT_STATUS_ID) VALUES (?, ?, ?, ?, ?, ?)", new String[]{"ID"});
            statement.setInt(1, nextId);
            statement.setInt(2, paymentRequest.getBookingId());
            statement.setObject(3, paymentRequest.getDate());
            statement.setInt(4, paymentRequest.getPayment());
            statement.setInt(5, paymentRequest.getPaymentTypeId());
            statement.setInt(6, paymentRequest.getPaymentStatusId());
            statement.executeUpdate();

            var payment = new Payments();
            payment.setId(nextId);
            payment.setBookingId(paymentRequest.getBookingId());
            payment.setDate(paymentRequest.getDate());
            payment.setPayment(paymentRequest.getPayment());
            payment.setPaymentTypeId(paymentRequest.getPaymentTypeId());
            payment.setPaymentStatusId(paymentRequest.getPaymentStatusId());

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
    public ResponseEntity<Payments> updatePayment(@PathVariable Integer id, @RequestBody PaymentRequest paymentRequest) {
        try {
            var statement = jdbcConnection.prepareStatement("UPDATE NBP09.NBP_PAYMENTS SET BOOKING_ID = ?, PAYMENT_DATE = ?, PAYMENT = ?, PAYMENT_TYPE_ID = ?, PAYMENT_STATUS_ID = ? WHERE ID = ?");
            statement.setInt(1, paymentRequest.getBookingId());
            statement.setObject(2, paymentRequest.getDate());
            statement.setInt(3, paymentRequest.getPayment());
            statement.setInt(4, paymentRequest.getPaymentTypeId());
            statement.setInt(5, paymentRequest.getPaymentStatusId());
            statement.setInt(6, id);
            statement.executeUpdate();
            var payment = new Payments();
            payment.setId(id);
            payment.setBookingId(paymentRequest.getBookingId());
            payment.setDate(paymentRequest.getDate());
            payment.setPayment(paymentRequest.getPayment());
            payment.setPaymentTypeId(paymentRequest.getPaymentTypeId());
            payment.setPaymentStatusId(paymentRequest.getPaymentStatusId());

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

    @GetMapping("/filter")
    public ResponseEntity<List<Payments>> filterPayments(
            @RequestParam(required = false) Integer bookingId,
            @RequestParam(required = false) Integer paymentTypeId,
            @RequestParam(required = false) Integer paymentStatusId,
            @RequestParam(required = false) Integer paymentMin,
            @RequestParam(required = false) Integer paymentMax,
            @RequestParam(required = false) String paymentDateFrom,
            @RequestParam(required = false) String paymentDateTo
    ) {
        try {
            StringBuilder query = new StringBuilder("SELECT * FROM NBP09.NBP_PAYMENTS WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (bookingId != null) {
                query.append(" AND BOOKING_ID = ?");
                params.add(bookingId);
            }
            if (paymentTypeId != null) {
                query.append(" AND PAYMENT_TYPE_ID = ?");
                params.add(paymentTypeId);
            }
            if (paymentStatusId != null) {
                query.append(" AND PAYMENT_STATUS_ID = ?");
                params.add(paymentStatusId);
            }
            if (paymentMin != null) {
                query.append(" AND PAYMENT >= ?");
                params.add(paymentMin);
            }
            if (paymentMax != null) {
                query.append(" AND PAYMENT <= ?");
                params.add(paymentMax);
            }
            if (paymentDateFrom != null) {
                query.append(" AND PAYMENT_DATE >= TO_TIMESTAMP(?, 'YYYY-MM-DD')");
                params.add(paymentDateFrom);
            }
            if (paymentDateTo != null) {
                query.append(" AND PAYMENT_DATE <= TO_TIMESTAMP(?, 'YYYY-MM-DD')");
                params.add(paymentDateTo);
            }

            var statement = jdbcConnection.prepareStatement(query.toString());
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }

            var resultSet = statement.executeQuery();
            var result = new ArrayList<Payments>();
            while (resultSet.next()) {
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
            log.error("Error filtering payments", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }


}
