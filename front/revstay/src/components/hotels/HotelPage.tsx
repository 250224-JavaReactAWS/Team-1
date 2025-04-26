/**
 * `HotelPage` is a React functional component for displaying a list of all hotels.
 * 
 * This component fetches hotel data from the backend API and displays it using the `HotelList` component.
 * It also handles loading and error states during the data-fetching process.
 * 
 * State:
 * - `hotels` (Hotel[]): An array of hotel objects fetched from the API.
 * - `loading` (boolean): Indicates whether the data is being loaded.
 * - `error` (string): Stores any error message encountered during the data-fetching process.
 * 
 * Methods:
 * - `fetchHotels`: An asynchronous function that retrieves hotel data from the API.
 * 
 * API Endpoint:
 * - GET `http://52.90.96.54:8080/hotels`: Fetches the list of all hotels.
 * 
 * UI Elements:
 * - `Container`: A Material-UI container for layout and spacing.
 * - `Typography`: Displays the page title.
 * - `CircularProgress`: A loading spinner displayed while data is being fetched.
 * - `Alert`: Displays an error message if the data-fetching process fails.
 * - `HotelList`: A child component that renders the list of hotels.
 * 
 * Behavior:
 * - If the data is still loading, a spinner is displayed.
 * - If an error occurs during data fetching, an error message is displayed.
 * - If the data is successfully fetched, the list of hotels is displayed.
 * 
 * Example Usage:
 * ```tsx
 * <HotelPage />
 * ```
 */

import React, { useEffect, useState } from "react";
import axios from "axios";
import { CircularProgress, Container, Typography, Alert } from "@mui/material";
import HotelList from "./templates/HotelList";

const HotelPage: React.FC = () => {
  const [hotels, setHotels] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchHotels = async () => {
      try {
        const response = await axios.get("http://52.90.96.54:8080/hotels");
        setHotels(response.data);
      } catch (err) {
        setError("Error at fetching hotels");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchHotels();
  }, []);

  return (
    <Container sx={{ mt: 4 }}>
      <Typography variant="h4" gutterBottom>
        Hotels List
      </Typography>

      {loading && <CircularProgress />}
      {error && <Alert severity="error">{error}</Alert>}
      {!loading && !error && <HotelList hotels={hotels} />}
    </Container>
  );
};

export default HotelPage;
