import React, { useState, useEffect } from "react";
import axios from "axios";
import { useLocation, useNavigate } from "react-router-dom";
import {
  Box,
  Typography,
  Card,
  CardContent,
  TextField,
  Button,
  Grid,
  Alert,
} from "@mui/material";

const BookingRoom: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { room } = location.state || {}; // Safely destructure location.state

  const [checkIn, setCheckIn] = useState("");
  const [checkOut, setCheckOut] = useState("");
  const [numGuests, setNumGuests] = useState(1);
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(true); // To handle loading state

  // Check if the user is logged in
  useEffect(() => {
    const checkSession = async () => {
      try {
        const response = await axios.get(
          "http://52.90.96.54:8080/users/session",
          { withCredentials: true }
        );
        if (response.status === 200) {
          console.log("User is logged in:", response.data);
        }
      } catch (error: any) {
        console.error("Error checking session:", error);
        if (error.response?.status === 401 || error.response?.status === 500) {
          setMessage("You are not logged in. Redirecting to login...");
          setTimeout(() => navigate("/login"), 2000); // Redirect to login after 2 seconds
          alert("Please log in to continue. Redirecting to login...");
        }
      } finally {
        setLoading(false);
      }
    };

    checkSession();
  }, []);

  // Redirect if room is missing
  useEffect(() => {
    if (!room) {
      setMessage("Room details are missing. Redirecting...");
      setTimeout(() => navigate(-1), 2000); // Redirect back after 2 seconds
    }
  }, [room, navigate]);

  const handleReservation = async (e: React.FormEvent) => {
    e.preventDefault();

    if (numGuests > room.maxGuests) {
      setMessage(
        `The number of guests exceeds the maximum allowed (${room.maxGuests}).`
      );
      return;
    }

    try {
      console.log({
        hotelId: room.hotelId, // Extracted hotelId from location.state
        roomId: room.roomId,
        checkIn,
        checkOut,
        guests: numGuests,
      });

      const response = await axios.post(
        "http://52.90.96.54:8080/bookings/reserve",
        {
          hotel: {
            hotelId: room.hotelId, // Extracted hotelId from location.state
          },
          room: {
            roomId: room.roomId, // Extracted roomId from location.state
          },
          checkIn,
          checkOut,
          guests: numGuests,
        },
        { withCredentials: true } // Include session cookies
      );
      if (response.status === 400) {
        setMessage(`Error: ${response.data.message || "Invalid request."}`);
      } else if (response.status === 200) {
        setMessage("Reservation successful!");

        setTimeout(
          () => navigate("/bookings/reserve", { replace: true }),
          2000
        );
      } else {
        setMessage("Failed to make a reservation.");
      }
    } catch (error: any) {
      console.error(error);
      setMessage("An error occurred while making the reservation.");
    }
  };

  if (loading) {
    return (
      <Box p={4}>
        <Typography variant="h6">Loading...</Typography>
      </Box>
    );
  }

  if (!room) {
    return (
      <Box p={4}>
        <Typography variant="h6" color="error">
          Room details are missing. Redirecting...
        </Typography>
      </Box>
    );
  }

  return (
    <Box p={4}>
      <Typography variant="h4" gutterBottom>
        Booking Room
      </Typography>

      {/* Room Details Block */}
      <Card elevation={3} sx={{ mb: 4 }}>
        <CardContent>
          <Typography variant="h6">{room.roomType}</Typography>
          <Typography variant="body2" color="text.secondary" gutterBottom>
            {room.description || "No description available"}
          </Typography>
          <Typography variant="subtitle1" color="primary">
            Price: ${room.price.toFixed(2)}
          </Typography>
          <Typography variant="caption" color="text.secondary">
            Maximum Capacity: {room.maxGuests} persons
          </Typography>
        </CardContent>
      </Card>

      {/* Booking Form */}
      <form onSubmit={handleReservation}>
        <Grid container spacing={3}>
          <Grid component="div">
            <TextField
              fullWidth
              label="Number of Guests"
              type="number"
              inputProps={{ min: 1, max: room.maxGuests }}
              value={numGuests}
              onChange={(e) => setNumGuests(Number(e.target.value))}
              required
            />
          </Grid>
          <Grid component="div">
            <TextField
              fullWidth
              label="Check-In Date and Time"
              type="datetime-local" // Changed to datetime-local
              InputLabelProps={{ shrink: true }}
              value={checkIn}
              onChange={(e) => setCheckIn(e.target.value)}
              required
            />
          </Grid>
          <Grid component="div">
            <TextField
              fullWidth
              label="Check-Out Date and Time"
              type="datetime-local" // Changed to datetime-local
              InputLabelProps={{ shrink: true }}
              value={checkOut}
              onChange={(e) => setCheckOut(e.target.value)}
              required
            />
          </Grid>
          <Grid component="div">
            <Button variant="contained" color="primary" type="submit" fullWidth>
              Reserve
            </Button>
          </Grid>
        </Grid>
      </form>

      {/* Message Display */}
      {message && (
        <Box mt={3}>
          <Alert
            severity={message.includes("successful") ? "success" : "error"}
          >
            {message}
          </Alert>
        </Box>
      )}
    </Box>
  );
};

export default BookingRoom;
