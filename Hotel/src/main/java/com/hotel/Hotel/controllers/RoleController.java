package com.hotel.Hotel.controllers;

import com.hotel.Hotel.common.request.RoleRequest;
import com.hotel.Hotel.models.Address;
import com.hotel.Hotel.models.Role;
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
@RequestMapping("/api/roles")
public class RoleController {
    private final Connection jdbcConnection;

    // Get all addresses
    @GetMapping("")
    public ResponseEntity<List<Role>> getAll() {
        try {
            var resultSet = jdbcConnection.createStatement().executeQuery("SELECT * FROM NBP.NBP_ROLE");
            var result = new ArrayList<Role>();
            while (resultSet.next()) {
                Role roles = new Role();
                roles.setId(resultSet.getInt("ID"));
                roles.setName(resultSet.getString("NAME"));
                result.add(roles);
            }
            return ResponseEntity.ok(result);
        } catch (SQLException e) {
            log.error("Error fetching role", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }
    @GetMapping(path="{id}")
    public ResponseEntity<Role> getById(@PathVariable Integer id) {
        try {
            var resultSet = jdbcConnection.createStatement().executeQuery("SELECT * FROM NBP.NBP_ROLE WHERE id='" + id + "'");
            System.out.println(resultSet);
            var role = new Role();
            while (resultSet.next()) {
                role.setId(resultSet.getInt("ID"));
                role.setName(resultSet.getString("NAME"));
            }
            return ResponseEntity.ok(role);
        } catch (SQLException e) {
            log.error("Error fetching role", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Role());
        }
    }

    @PostMapping()
    public ResponseEntity<Role> saveRole(@RequestBody RoleRequest roleRequest) {
        Role saveRole;
        Integer roleId=1;
        try {
            ResultSet resultSet=jdbcConnection.prepareStatement("SELECT MAX(ID) FROM NBP.NBP_ROLE").executeQuery();
            if(resultSet.next())
            {
                roleId=resultSet.getInt(1)+1;
            }
        }
        catch (SQLException e) {
            log.error("Error creating role", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Role());
        }
        try {
            saveRole = new Role(
                    roleId,
                    roleRequest.getName()
            );
            var prepareStatement = jdbcConnection.prepareStatement("INSERT INTO NBP.NBP_ROLE (ID, NAME) VALUES(?, ?)");
            prepareStatement.setInt(1,saveRole.getId());
            prepareStatement.setString(2,saveRole.getName());
            var resultSet=prepareStatement.executeQuery();
            return ResponseEntity.ok(saveRole);
        } catch (SQLException e) {
            log.error("Error fetching role", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Role());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Integer id, @RequestBody RoleRequest roleRequest) {
        try {
            var updatedRole=new Role(
                    id,
                    roleRequest.getName()
            );
            var statement = jdbcConnection.prepareStatement("UPDATE NBP.NBP_ROLE SET NAME = ?");
            statement.setString(1, roleRequest.getName());
            var resultSet=statement.executeUpdate();

            return ResponseEntity.ok(updatedRole);
        } catch (SQLException e) {
            log.error("Error updating role", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Role());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Integer id) {
        try {
            var statement = jdbcConnection.prepareStatement("DELETE FROM NBP.NBP_ROLE WHERE ID = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            return ResponseEntity.noContent().build();
        } catch (SQLException e) {
            log.error("Error deleting role", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
