package com.hotel.Hotel.service;

import com.hotel.Hotel.models.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {

    private final Connection jdbcConnection;

    public Role getRoleById(int roleId) {
        try {
            var resultSet = jdbcConnection.createStatement().executeQuery("SELECT * FROM NBP.NBP_ROLE WHERE ID=" + roleId);
            if (resultSet.next()) {
                Role role = new Role();
                role.setId(resultSet.getInt("ID"));
                role.setName(resultSet.getString("NAME"));
                return role;
            }
        } catch (SQLException e) {
            log.error("Error fetching role with ID {}", roleId, e);
        }
        return null;
    }

}
