package com.hotel.Hotel.controllers;

import com.hotel.Hotel.helpers.ImageHelper;
import com.hotel.Hotel.models.Image;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class ImageController {
    private final Connection jdbcConnection;

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<Image>> getImagesByRoom(@PathVariable Integer roomId){
        List<Image> images = new ArrayList<>();
        try {
            var statement = jdbcConnection.prepareStatement("SELECT * FROM NBP09.NBP_IMAGE WHERE ROOM_ID = ?");
            statement.setInt(1, roomId);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                var image = new Image();
                image.setId(resultSet.getInt("ID"));
                image.setName(resultSet.getString("NAME"));
                image.setType(resultSet.getString("TYPE"));
                image.setImageData(resultSet.getBytes("IMAGE_DATA"));
                image.setRoomId(roomId);
                images.add(image);
            } else {
                return ResponseEntity.notFound().build();
            }
            for (Image image:images) {
                image.setImageData(ImageHelper.decompressImage(image.getImageData()));
            }
            return ResponseEntity.ok().body(images);
        } catch (Exception e) {
            log.error("Error fetching image for room", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Image> addRoomImage(@RequestPart("file") MultipartFile file, @RequestPart("roomId") Integer roomId) {
        try {
            Image pImage = new Image();
            pImage.setImageData(ImageHelper.compressImage(file.getBytes()));
            pImage.setName(file.getOriginalFilename());
            pImage.setType(file.getContentType());
            var maxIdStatement = jdbcConnection.prepareStatement("SELECT COALESCE(MAX(ID), 0) + 1 AS NEXT_ID FROM NBP09.NBP_IMAGE");
            var resultSet = maxIdStatement.executeQuery();
            int nextId = 1; // Default to 1 if the table is empty
            if (resultSet.next()) {
                nextId = resultSet.getInt("NEXT_ID");
            }

            var statement = jdbcConnection.prepareStatement("INSERT INTO NBP09.NBP_IMAGE (ID, NAME, TYPE, IMAGE_DATA, ROOM_ID) VALUES (?, ?, ?, ?, ?)", new String[]{"ID"});
            statement.setInt(1, nextId);
            statement.setString(2, pImage.getName());
            statement.setString(3, pImage.getType());
            statement.setBytes(4, pImage.getImageData());
            statement.setInt(5, roomId);
            statement.executeUpdate();

            var imageResponse = new Image();
            imageResponse.setId(nextId);
            imageResponse.setName(pImage.getName());
            imageResponse.setType(pImage.getType());
            imageResponse.setRoomId(roomId);
            imageResponse.setImageData(pImage.getImageData());

            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                imageResponse.setId(generatedKeys.getInt(1));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(imageResponse);
        } catch (SQLException e) {
            log.error("Error creating image", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
