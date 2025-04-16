package com.Rev.RevStay.DTOS;


import com.Rev.RevStay.models.Hotel;


import java.math.BigDecimal;



public class RoomDTO {


    private int roomId;
    private int hotelId;
    private String hotelName;
    private String roomType;
    private String description;
    private BigDecimal price;
    private int maxGuests;

    public RoomDTO(int roomId, int hotelId, String roomType, String description, BigDecimal price, int maxGuests) {
        this.roomId = roomId;
        this.hotelId = hotelId;
        this.roomType = roomType;
        this.description = description;
        this.price = price;
        this.maxGuests = maxGuests;
    }

    public RoomDTO(com.Rev.RevStay.models.Room room) {
        this.roomId = room.getRoomId();
        this.price = room.getPrice();
        this.maxGuests = room.getMaxGuests();
        this.description = room.getDescription();
        this.roomType = room.getRoomType();
        this.hotelId = room.getHotel().getHotelId();
        this.hotelName = room.getHotel().getName();
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getHotelId() { return hotelId; }

    public void setHotelId(int hotelId) { this.hotelId = hotelId; }

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

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

}
