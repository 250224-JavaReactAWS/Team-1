/**
 * `BookingForm` is a React functional component for creating a booking form.
 * 
 * This component allows users to input booking details such as check-in date, 
 * check-out date, and the number of guests. It sends the booking data to the 
 * backend API for reservation processing.
 * 
 * Props:
 * - `hotelId` (number): The ID of the hotel for the booking.
 * - `roomId` (number): The ID of the room for the booking.
 * 
 * State:
 * - `checkIn` (string): The selected check-in date and time.
 * - `checkOut` (string): The selected check-out date and time.
 * - `guests` (number): The number of guests for the booking.
 * - `error` (string | null): Error message to display if the booking fails.
 * - `success` (string | null): Success message to display if the booking is successful.
 * 
 * Methods:
 * - `handleSubmit`: Handles the form submission, sends booking data to the API, 
 *   and updates the success or error state based on the response.
 * 
 * API Endpoint:
 * - POST `http://52.90.96.54:8080/bookings/reserve`: Sends booking data to the backend.
 * 
 * UI Elements:
 * - `TextField`: Material-UI input fields for check-in, check-out, and number of guests.
 * - `Button`: Material-UI button to submit the form.
 * - `Typography`: Displays error or success messages.
 * - `Box`: Material-UI container for styling the form.
 * 
 * Example Usage:
 * ```tsx
 * <BookingForm hotelId={1} roomId={101} />
 * ```
 */

import React, { useState } from "react";
import axios from "axios";
import { Box, Button, TextField, Typography } from "@mui/material";

interface BookingFormProps {
  hotelId: number;
  roomId: number;
}

const BookingForm: React.FC<BookingFormProps> = ({ hotelId, roomId }) => {
  const [checkIn, setCheckIn] = useState("");
  const [checkOut, setCheckOut] = useState("");
  const [guests, setGuests] = useState(1);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const bookingData = {
      checkIn,
      checkOut,
      guests,
      hotel: {
        hotelId,
      },
      room: {
        roomId,
      },
    };

    try {
      const response = await axios.post(
        "http://52.90.96.54:8080/bookings/reserve",
        bookingData,
        { withCredentials: true }
      );
      console.log("Booking successful:", response.data);
      setSuccess("Reservation successfully made.");
      setError(null);
    } catch (err) {
      console.error("Error making booking:", err);
      setError("Could not make the reservation. Please try again.");
      setSuccess(null);
    }
  };

  return (
    <Box
      component="form"
      onSubmit={handleSubmit}
      sx={{
        maxWidth: 400,
        margin: "auto",
        padding: 4,
        border: "1px solid #ccc",
        borderRadius: 2,
        boxShadow: 2,
      }}
    >
      <Typography variant="h5" gutterBottom>
        Make a Reservation
      </Typography>
      {error && (
        <Typography color="error" gutterBottom>
          {error}
        </Typography>
      )}
      {success && (
        <Typography color="primary" gutterBottom>
          {success}
        </Typography>
      )}
      <TextField
        label="Check-In Date"
        type="datetime-local"
        value={checkIn}
        onChange={(e) => setCheckIn(e.target.value)}
        fullWidth
        required
        margin="normal"
        InputLabelProps={{
          shrink: true,
        }}
      />
      <TextField
        label="Check-Out Date"
        type="datetime-local"
        value={checkOut}
        onChange={(e) => setCheckOut(e.target.value)}
        fullWidth
        required
        margin="normal"
        InputLabelProps={{
          shrink: true,
        }}
      />
      <TextField
        label="Number of Guests"
        type="number"
        value={guests}
        onChange={(e) => setGuests(Number(e.target.value))}
        fullWidth
        required
        margin="normal"
        inputProps={{ min: 1 }}
      />
      <Button type="submit" variant="contained" color="primary" fullWidth>
        Reserve
      </Button>
    </Box>
  );
};

export default BookingForm;
