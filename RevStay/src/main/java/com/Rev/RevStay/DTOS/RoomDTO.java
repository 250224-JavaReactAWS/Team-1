package com.Rev.RevStay.DTOS;


import com.Rev.RevStay.models.Hotel;


import java.math.BigDecimal;



public class RoomDTO {


    private int roomId;

    private Hotel hotel;

    private String roomType;

    private String description;

    private BigDecimal price;

    private int maxGuests;

    public RoomDTO() {}
    public RoomDTO(int roomId, Hotel hotel, String roomType, String description, BigDecimal price, int maxGuests) {
        this.roomId = roomId;
        this.hotel = hotel;
        this.roomType = roomType;
        this.description = description;
        this.price = price;
        this.maxGuests = maxGuests;
    }
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
