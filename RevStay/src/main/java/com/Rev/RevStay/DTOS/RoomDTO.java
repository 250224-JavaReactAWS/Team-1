package com.Rev.RevStay.DTOS;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) class for transferring room-related data.
 * 
 * This class is used to encapsulate room information and transfer it between
 * different layers of the application (e.g., service and controller layers)
 * without exposing the full `Room` entity.
 * 
 * Fields:
 * - `roomId`: The unique identifier of the room.
 * - `hotelId`: The unique identifier of the hotel to which the room belongs.
 * - `hotelName`: The name of the hotel to which the room belongs.
 * - `roomType`: The type of the room (e.g., single, double, suite).
 * - `description`: A description of the room.
 * - `price`: The price of the room.
 * - `maxGuests`: The maximum number of guests allowed in the room.
 * 
 * Constructors:
 * - Allows creating `RoomDTO` objects from individual fields or directly from a
 * `Room` entity.
 */
public class RoomDTO {

    private int roomId;
    private int hotelId;
    private String hotelName;
    private String roomType;
    private String description;
    private BigDecimal price;
    private int maxGuests;

    /**
     * Constructor for creating a RoomDTO with all fields.
     * 
     * @param roomId      The unique identifier of the room.
     * @param hotelId     The unique identifier of the hotel to which the room
     *                    belongs.
     * @param roomType    The type of the room (e.g., single, double, suite).
     * @param description A description of the room.
     * @param price       The price of the room.
     * @param maxGuests   The maximum number of guests allowed in the room.
     */
    public RoomDTO(int roomId, int hotelId, String roomType, String description, BigDecimal price, int maxGuests) {
        this.roomId = roomId;
        this.hotelId = hotelId;
        this.roomType = roomType;
        this.description = description;
        this.price = price;
        this.maxGuests = maxGuests;
    }

    /**
     * Constructor for creating a RoomDTO from a Room entity.
     * 
     * @param room The Room entity to convert into a RoomDTO.
     */
    public RoomDTO(com.Rev.RevStay.models.Room room) {
        this.roomId = room.getRoomId();
        this.price = room.getPrice();
        this.maxGuests = room.getMaxGuests();
        this.description = room.getDescription();
        this.roomType = room.getRoomType();
        this.hotelId = room.getHotel().getHotelId();
        this.hotelName = room.getHotel().getName();
    }

    // Getters and setters for all fields.

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }
}
