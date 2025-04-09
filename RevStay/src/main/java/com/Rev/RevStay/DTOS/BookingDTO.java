package com.Rev.RevStay.DTOS;

import com.Rev.RevStay.models.BookingStatus;
import com.Rev.RevStay.models.Hotel;
import com.Rev.RevStay.models.Room;
import com.Rev.RevStay.models.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public class BookingDTO {


    private int bookId;
    private User user;
    private Hotel hotel;
    private Room room;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private int guests;
    private BookingStatus status;
    private LocalDateTime createdAt;


    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
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

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getUserId() {
        return user.getUserId();
    }

    public void setStatusAccepted() {
        status = BookingStatus.ACCEPTED;
    }

    public void setStatusCancelled() {
        status = BookingStatus.CANCELLED;
    }

}

