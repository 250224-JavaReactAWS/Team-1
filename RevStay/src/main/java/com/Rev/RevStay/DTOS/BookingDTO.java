package com.Rev.RevStay.DTOS;

import com.Rev.RevStay.models.BookingStatus;
import com.Rev.RevStay.models.Hotel;
import com.Rev.RevStay.models.Room;
import com.Rev.RevStay.models.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public class BookingDTO {

    private int bookingId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private int guests;
    private String status;
    private int hotelId;
    private String hotelName;
    private int roomId;
    private String roomType;

    public BookingDTO(int bookingId, LocalDateTime checkIn, LocalDateTime checkOut, int guests,
                      String status, int hotelId, String hotelName, int roomId, String roomType) {
        this.bookingId = bookingId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.guests = guests;
        this.status = status;
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.roomId = roomId;
        this.roomType = roomType;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public LocalDateTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDateTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDateTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDateTime checkOut) {
        this.checkOut = checkOut;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
}

