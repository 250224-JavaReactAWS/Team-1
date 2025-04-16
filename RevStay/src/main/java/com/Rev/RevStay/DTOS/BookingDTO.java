package com.Rev.RevStay.DTOS;

import java.time.LocalDateTime;
import java.util.Objects;

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
    private String userEmail;

    public BookingDTO(int bookingId, LocalDateTime checkIn, LocalDateTime checkOut, int guests,
                      String status, int hotelId, String hotelName, int roomId, String roomType, String userEmail) {
        this.bookingId = bookingId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.guests = guests;
        this.status = status;
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.roomId = roomId;
        this.roomType = roomType;
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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
    // Fields and constructor

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingDTO that = (BookingDTO) o;
        return Objects.equals(bookingId, that.bookingId) &&
               Objects.equals(checkIn, that.checkIn) &&
               Objects.equals(checkOut, that.checkOut) &&
               guests == that.guests &&
               Objects.equals(status, that.status) &&
               hotelId == that.hotelId &&
               Objects.equals(hotelName, that.hotelName) &&
               roomId == that.roomId &&
               Objects.equals(roomType, that.roomType) &&
               Objects.equals(userEmail, that.userEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, checkIn, checkOut, guests, status, hotelId, hotelName, roomId, roomType, userEmail);
    }


}

