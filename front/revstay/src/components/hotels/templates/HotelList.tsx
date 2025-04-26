import { useContext, useState, useEffect } from "react";
import { Grid, Box } from "@mui/material";
import axios from "axios";
import { authContext } from "../../../App";
import HotelCard from "./HotelCard";

/**
 * `HotelList` is a React functional component for displaying a list of hotels.
 * 
 * This component renders a grid of hotel cards, each showing details about a specific hotel.
 * It also manages the user's favorite hotels and allows toggling the favorite status.
 * 
 * Props:
 * - `hotels` (Hotel[]): An array of hotel objects to display.
 * 
 * Hotel Object Structure:
 * - `hotelId` (number): The unique identifier for the hotel.
 * - `name` (string): The name of the hotel.
 * - `location` (string): The location of the hotel.
 * - `description` (string): A brief description of the hotel.
 * - `amenities` (string): A comma-separated list of amenities offered by the hotel.
 * - `priceRange` (string): The price range of the hotel.
 * - `images` (string[]): An array of image URLs for the hotel.
 * - `createdAt` (string): The creation date of the hotel.
 * - `ownerEmail` (string): The email of the hotel's owner.
 * - `ownerFullName` (string): The full name of the hotel's owner.
 * 
 * State:
 * - `favoriteHotels` (number[]): An array of hotel IDs marked as favorites by the user.
 * 
 * Context:
 * - `authContext`: Provides the user's authentication and role information.
 * 
 * Methods:
 * - `fetchFavorites`: Fetches the user's favorite hotels from the backend API.
 * - `toggleFavorite`: Toggles the favorite status of a hotel by sending a POST or DELETE request to the backend API.
 * 
 * API Endpoints:
 * - GET `http://52.90.96.54:8080/hotels/favoritesUser`: Fetches the user's favorite hotels.
 * - POST `http://52.90.96.54:8080/users/favorites/{hotelId}`: Adds a hotel to the user's favorites.
 * - DELETE `http://52.90.96.54:8080/users/favorites/{hotelId}`: Removes a hotel from the user's favorites.
 * 
 * UI Elements:
 * - `Box`: A Material-UI container for layout and spacing.
 * - `Grid`: A Material-UI grid layout for organizing hotel cards.
 * - `HotelCard`: A child component that renders individual hotel details.
 * 
 * Behavior:
 * - If the user is logged in, their favorite hotels are fetched and displayed.
 * - Users can toggle the favorite status of a hotel by clicking the favorite button on the `HotelCard`.
 * 
 * Example Usage:
 * ```tsx
 * const hotels = [
 *   {
 *     hotelId: 1,
 *     name: "Luxury Hotel",
 *     location: "New York, NY",
 *     description: "A luxurious hotel in the heart of the city.",
 *     amenities: "Pool, Gym, Spa, Free WiFi",
 *     priceRange: "$200-$500",
 *     images: ["image1.jpg", "image2.jpg"],
 *     createdAt: "2025-04-25",
 *     ownerEmail: "owner@example.com",
 *     ownerFullName: "John Doe",
 *   },
 * ];
 * 
 * <HotelList hotels={hotels} />
 * ```
 */

// Define el tipo de hotel
type Hotel = {
  hotelId: number;
  name: string;
  location: string;
  description: string;
  amenities: string;
  priceRange: string;
  images: string[];
  createdAt: string;
  ownerEmail: string;
  ownerFullName: string;
};

interface Props {
  hotels: Hotel[];
}

const HotelList: React.FC<Props> = ({ hotels }) => {
  const [favoriteHotels, setFavoriteHotels] = useState<number[]>([]);
  const roleReference = useContext(authContext);
  const isLoggedIn = roleReference?.role !== "UNAUTHENTICATED";

  useEffect(() => {
    const fetchFavorites = async () => {
      try {
        console.log(isLoggedIn);
        if (isLoggedIn) {
          const response = await axios.get<Hotel[]>(
            "http://52.90.96.54:8080/hotels/favoritesUser",
            { withCredentials: true }
          );
          const favoriteIds = response.data.map((hotel) => hotel.hotelId);
          setFavoriteHotels(favoriteIds);
        }
      } catch (error) {
        console.error("Error fetching favorite hotels:", error);
      }
    };

    fetchFavorites();
  }, [isLoggedIn]);

  const toggleFavorite = async (hotelId: number) => {
    try {
      if (favoriteHotels.includes(hotelId)) {
        // Si el hotel ya está en favoritos, realiza una solicitud DELETE
        await axios.delete(
          `http://52.90.96.54:8080/users/favorites/${hotelId}`,
          { withCredentials: true }
        );

        // Elimina el hotel de la lista de favoritos
        setFavoriteHotels((prevFavorites) =>
          prevFavorites.filter((id) => id !== hotelId)
        );
      } else {
        // Si el hotel no está en favoritos, realiza una solicitud POST
        await axios.post(
          `http://52.90.96.54:8080/users/favorites/${hotelId}`,
          {},
          { withCredentials: true }
        );

        // Agrega el hotel a la lista de favoritos
        setFavoriteHotels((prevFavorites) => [...prevFavorites, hotelId]);
      }
    } catch (error) {
      console.error("Error toggling favorite hotel:", error);
    }
  };

  return (
    <Box p={4}>
      <Grid container spacing={3} justifyContent="center">
        {hotels.map((hotel) => (
          <Grid key={hotel.hotelId} size={4}>
            <HotelCard
              hotel={hotel}
              isFavorite={favoriteHotels.includes(hotel.hotelId)}
              toggleFavorite={toggleFavorite}
            />
          </Grid>
        ))}
      </Grid>
    </Box>
  );
};

export default HotelList;
