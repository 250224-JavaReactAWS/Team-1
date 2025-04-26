/**
 * `HotelFavorite` is a React functional component for displaying a list of the user's favorite hotels.
 * 
 * This component fetches the user's favorite hotels from the backend API and displays them using the `HotelList` component.
 * It also handles loading and error states during the data-fetching process.
 * 
 * State:
 * - `hotels` (Hotel[]): An array of hotel objects fetched from the API.
 * - `loading` (boolean): Indicates whether the data is being loaded.
 * - `error` (string): Stores any error message encountered during the data-fetching process.
 * 
 * Methods:
 * - `fetchHotels`: An asynchronous function that retrieves the user's favorite hotels from the API.
 * 
 * API Endpoint:
 * - GET `http://52.90.96.54:8080/hotels/favoritesUser`: Fetches the user's favorite hotels.
 * 
 * UI Elements:
 * - `Container`: A Material-UI container for layout and spacing.
 * - `Typography`: Displays the page title.
 * - `CircularProgress`: A loading spinner displayed while data is being fetched.
 * - `Alert`: Displays an error message if the data-fetching process fails.
 * - `HotelList`: A child component that renders the list of favorite hotels.
 * 
 * Behavior:
 * - If the data is still loading, a spinner is displayed.
 * - If an error occurs during data fetching, an error message is displayed.
 * - If the data is successfully fetched, the list of favorite hotels is displayed.
 * 
 * Example Usage:
 * ```tsx
 * <HotelFavorite />
 * ```
 */

import React, { useEffect, useState } from "react";
import axios from "axios";
import { CircularProgress, Container, Typography, Alert } from "@mui/material";
import HotelList from "./templates/HotelList";

const HotelFavorite: React.FC = () => {
  const [hotels, setHotels] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchHotels = async () => {
      try {
        const response = await axios.get(
          "http://52.90.96.54:8080/hotels/favoritesUser",
          { withCredentials: true }
        );
        setHotels(response.data);
      } catch (err) {
        setError("Error while obtaining the hotels");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchHotels();
  }, []);

  return (
    <Container sx={{ mt: 4, width: "100%", height: "100%" }}>
      <Typography variant="h4" gutterBottom>
        Favorite Hotels
      </Typography>
      {loading && <CircularProgress />}
      {error && <Alert severity="error">{error}</Alert>}
      {!loading && !error && <HotelList hotels={hotels} />}
    </Container>
  );
};

export default HotelFavorite;
