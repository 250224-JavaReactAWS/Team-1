package com.Rev.RevStay.DTOS;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Data Transfer Object (DTO) class for transferring booking-related data.
 * 
 * This class is used to encapsulate booking information and transfer it between
 * different layers of the application (e.g., service and controller layers)
 * without exposing the full `Booking` entity.
 * 
 * Fields:
 * - `bookingId`: The unique identifier of the booking.
 * - `checkIn`: The check-in date and time for the booking.
 * - `checkOut`: The check-out date and time for the booking.
 * - `guests`: The number of guests for the booking.
 * - `status`: The status of the booking (e.g., PENDING, CONFIRMED, CANCELLED).
 * - `hotelId`: The unique identifier of the hotel associated with the booking.
 * - `hotelName`: The name of the hotel associated with the booking.
 * - `roomId`: The unique identifier of the room associated with the booking.
 * - `roomType`: The type of the room associated with the booking.
 * - `userEmail`: The email address of the user who made the booking.
 * 
 * Constructors:
 * - Allows creating `BookingDTO` objects with all fields.
 * 
 * Getters and Setters:
 * - Provides methods to access and modify the fields.
 * 
 * Overrides:
 * - `equals` and `hashCode` methods for comparing `BookingDTO` objects.
 */
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

    /**
     * Constructor for creating a BookingDTO with all fields.
     * 
     * @param bookingId The unique identifier of the booking.
     * @param checkIn   The check-in date and time for the booking.
     * @param checkOut  The check-out date and time for the booking.
     * @param guests    The number of guests for the booking.
     * @param status    The status of the booking.
     * @param hotelId   The unique identifier of the hotel associated with the
     *                  booking.
     * @param hotelName The name of the hotel associated with the booking.
     * @param roomId    The unique identifier of the room associated with the
     *                  booking.
     * @param roomType  The type of the room associated with the booking.
     * @param userEmail The email address of the user who made the booking.
     */
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

    // Getters and setters for all fields.

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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BookingDTO that = (BookingDTO) o;
        return bookingId == that.bookingId &&
                guests == that.guests &&
                hotelId == that.hotelId &&
                roomId == that.roomId &&
                Objects.equals(checkIn, that.checkIn) &&
                Objects.equals(checkOut, that.checkOut) &&
                Objects.equals(status, that.status) &&
                Objects.equals(hotelName, that.hotelName) &&
                Objects.equals(roomType, that.roomType) &&
                Objects.equals(userEmail, that.userEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, checkIn, checkOut, guests, status, hotelId, hotelName, roomId, roomType,
                userEmail);
    }
}
