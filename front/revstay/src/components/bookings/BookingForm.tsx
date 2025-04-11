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
        "http://localhost:8080/bookings/reserve",
        bookingData,
        { withCredentials: true }
      );
      console.log("Booking successful:", response.data);
      setSuccess("Reservación realizada con éxito.");
      setError(null);
    } catch (err) {
      console.error("Error making booking:", err);
      setError("No se pudo realizar la reservación. Inténtalo de nuevo.");
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
        Realizar Reservación
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
        label="Fecha de Check-In"
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
        label="Fecha de Check-Out"
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
        label="Número de Huéspedes"
        type="number"
        value={guests}
        onChange={(e) => setGuests(Number(e.target.value))}
        fullWidth
        required
        margin="normal"
        inputProps={{ min: 1 }}
      />
      <Button type="submit" variant="contained" color="primary" fullWidth>
        Reservar
      </Button>
    </Box>
  );
};

export default BookingForm;