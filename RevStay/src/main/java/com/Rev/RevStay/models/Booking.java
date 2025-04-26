package com.Rev.RevStay.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entity class representing a booking in the system.
 * 
 * This class maps to the `bookings` table in the database and contains details
 * about:
 * - The user making the booking.
 * - The hotel and room associated with the booking.
 * - The check-in and check-out dates.
 * - The number of guests.
 * - The booking status.
 * - The creation timestamp.
 * 
 * Annotations:
 * - `@Entity`: Marks this class as a JPA entity.
 * - `@Table`: Specifies the table name in the database.
 * - `@Id`: Marks the primary key of the entity.
 * - `@GeneratedValue`: Specifies the generation strategy for the primary key.
 * - `@ManyToOne`: Defines many-to-one relationships with `User`, `Hotel`, and
 * `Room`.
 * - `@JoinColumn`: Specifies the foreign key columns for relationships.
 * - `@Column`: Maps fields to database columns and specifies constraints.
 * - `@Enumerated`: Maps the `BookingStatus` enum to a string column in the
 * database.
 */
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "hotelId", nullable = false)
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "roomId", nullable = false)
    private Room room;

    @Column(name = "check_in", nullable = false)
    private LocalDateTime checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDateTime checkOut;

    @Column(nullable = false)
    private int guests;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Default constructor for the Booking class.
     */
    public Booking() {
    }

    // Getters and setters for all fields.

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

    /**
     * Retrieves the user ID associated with the booking.
     * 
     * @return The user ID.
     */
    public int getUserId() {
        return user.getUserId();
    }

    // Methods to set specific booking statuses.

    public void setStatusAccepted() {
        status = BookingStatus.ACCEPTED;
    }

    public void setStatusCancelled() {
        status = BookingStatus.CANCELLED;
    }

    public void setStatusPending() {
        status = BookingStatus.PENDING;
    }

    public void setStatusCompleted() {
        status = BookingStatus.COMPLETED;
    }

    public void setStatusConfirmed() {
        status = BookingStatus.CONFIRMED;
    }

}
