/**
 * `BookingPage` is a React functional component for displaying a user's list of bookings.
 * 
 * This component fetches booking data from the backend API and displays it using the `BookingList` component.
 * It also handles loading and error states during the data-fetching process.
 * 
 * State:
 * - `bookings` (Booking[]): An array of booking objects fetched from the API.
 * - `loading` (boolean): Indicates whether the data is being loaded.
 * - `error` (string): Stores any error message encountered during the data-fetching process.
 * 
 * Methods:
 * - `fetchBookings`: An asynchronous function that retrieves booking data from the API.
 * 
 * API Endpoint:
 * - GET `http://52.90.96.54:8080/bookings/user`: Fetches the bookings for the logged-in user.
 * 
 * UI Elements:
 * - `Container`: A Material-UI container for layout and spacing.
 * - `Typography`: Displays the page title.
 * - `CircularProgress`: A loading spinner displayed while data is being fetched.
 * - `Alert`: Displays an error message if the data-fetching process fails.
 * - `BookingList`: A child component that renders the list of bookings.
 * 
 * Example Usage:
 * ```tsx
 * <BookingPage />
 * ```
 */

import { Alert, CircularProgress, Container, Typography } from "@mui/material";
import axios from "axios";
import React, { useEffect, useState } from "react";
import BookingList from "./BookingList";

const BookingPage: React.FC = () => {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchBookings = async () => {
      try {
        const response = await axios.get(
          "http://52.90.96.54:8080/bookings/user",
          { withCredentials: true }
        );
        setBookings(response.data);
      } catch (err) {
        setError("Error while fetching bookings");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchBookings();
  }, []);

  return (
    <Container sx={{ mt: 4 }}>
      <Typography variant="h4" gutterBottom>
        Bookings List
      </Typography>

      {loading && <CircularProgress />}
      {error && <Alert severity="error">{error}</Alert>}
      {!loading && !error && (
        <BookingList bookings={bookings} isLoggedIn={false} />
      )}
    </Container>
  );
};

export default BookingPage;
