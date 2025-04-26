/**
 * `HotelCard` is a React functional component for displaying hotel details in a card format.
 *
 * This component shows information about a hotel, including its name, location, description,
 * amenities, price range, and images. It also provides actions for navigating to the hotel's
 * rooms, updating the hotel, deleting the hotel, and toggling it as a favorite.
 *
 * Props:
 * - `hotel` (object): The hotel details.
 *   - `hotelId` (number): The unique identifier for the hotel.
 *   - `name` (string): The name of the hotel.
 *   - `location` (string): The location of the hotel.
 *   - `description` (string): A brief description of the hotel.
 *   - `amenities` (string): A comma-separated list of amenities offered by the hotel.
 *   - `priceRange` (string): The price range of the hotel.
 *   - `images` (string[]): An array of image URLs for the hotel.
 *   - `createdAt` (string): The creation date of the hotel.
 *   - `ownerEmail` (string): The email of the hotel's owner.
 *   - `ownerFullName` (string): The full name of the hotel's owner.
 * - `isFavorite` (boolean): Indicates whether the hotel is marked as a favorite by the user.
 * - `toggleFavorite` (function): A function to toggle the favorite status of the hotel.
 *
 * State:
 * - `showAllAmenities` (boolean): Controls whether all amenities are displayed or truncated.
 *
 * Methods:
 * - `handleRoomNavigation`: Navigates to the hotel's room list page.
 * - `handleUpdateHotel`: Navigates to the hotel update page.
 * - `handleDeleteHotel`: Navigates to the hotel deletion page.
 *
 * UI Elements:
 * - `Card`: Displays the hotel details in a styled card format.
 * - `CardHeader`: Displays the hotel name and location, with an optional favorite toggle button.
 * - `CardContent`: Displays the hotel description, price range, and amenities.
 * - `Button`: Provides actions for navigating to rooms, updating, or deleting the hotel.
 * - `Chip`: Displays individual amenities as styled chips.
 * - `HotelImageGallery`: A child component for displaying the hotel's image gallery.
 *
 * Behavior:
 * - If the user is logged in and has the "USER" role, they can toggle the hotel as a favorite.
 * - If the user has the "OWNER" role, they can update or delete the hotel.
 * - Amenities are truncated by default, with an option to expand or collapse the list.
 *
 * Example Usage:
 * ```tsx
 * <HotelCard
 *   hotel={{
 *     hotelId: 1,
 *     name: "Luxury Hotel",
 *     location: "New York, NY",
 *     description: "A luxurious hotel in the heart of the city.",
 *     amenities: "{Pool, Gym, Spa, Free WiFi}",
 *     priceRange: "$200-$500",
 *     images: ["image1.jpg", "image2.jpg"],
 *     createdAt: "2025-04-25",
 *     ownerEmail: "owner@example.com",
 *     ownerFullName: "John Doe",
 *   }}
 *   isFavorite={true}
 *   toggleFavorite={(hotelId) => console.log(`Toggled favorite for hotel ${hotelId}`)}
 * />
 * ```
 */

import {
  Card,
  CardContent,
  Typography,
  CardHeader,
  IconButton,
  Button,
  Chip,
  Box,
} from "@mui/material";
import FavoriteIcon from "@mui/icons-material/Favorite";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";
import { useContext, useState } from "react";
import { authContext } from "../../../App";
import { useNavigate } from "react-router-dom";
import HotelImageGallery from "./HoltelImageGalery";

interface Props {
  hotel: {
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

  isFavorite: boolean;
  toggleFavorite: (hotelId: number) => void;
}

const HotelCard: React.FC<Props> = ({ hotel, isFavorite, toggleFavorite }) => {
  const navigate = useNavigate();
  const roleReference = useContext(authContext);
  const isLoggedIn = roleReference?.role !== "UNAUTHENTICATED";

  const [showAllAmenities, setShowAllAmenities] = useState(false);

  const handleRoomNavigation = (hotelId: number) => {
    navigate(`/rooms/hotel/${hotelId}`);
  };

  const handleUpdateHotel = (hotelId: number) => {
    navigate(`/hotels/update/${hotelId}`);
  };

  const handleDeleteHotel = (hotelId: number) => {
    navigate(`/hotels/delete/${hotelId}`);
  };

  const amenitiesArray = hotel.amenities
    .replace(/[{}]/g, "")
    .split(",")
    .map((a) => a.trim());

  return (
    <Card elevation={3}>
      <HotelImageGallery images={hotel.images} />
      <CardHeader
        title={hotel.name}
        subheader={hotel.location}
        sx={{ mt: 0, mb: 0, pb: 0 }}
        action={
          isLoggedIn &&
          roleReference?.role === "USER" && (
            <IconButton onClick={() => toggleFavorite(hotel.hotelId)}>
              {isFavorite ? (
                <FavoriteIcon color="error" />
              ) : (
                <FavoriteBorderIcon />
              )}
            </IconButton>
          )
        }
      />
      <CardContent>
        <Typography variant="body2" color="text.secondary" gutterBottom>
          {hotel.description}
        </Typography>
        <Typography variant="subtitle2" color="primary">
          Price: {hotel.priceRange} $
        </Typography>

        <Box mt={2} mb={1}>
          <Typography variant="caption">Amenities:</Typography>
          <Box
            display="flex"
            justifyContent="center"
            flexWrap="wrap"
            gap={1}
            mt={0.5}
            sx={{
              maxHeight: showAllAmenities ? 120 : 25,
              overflowY: "hidden",
              transition: "max-height 0.3s ease",
            }}
          >
            {amenitiesArray.map((amenity, index) => (
              <Chip
                key={index}
                label={amenity}
                size="small"
                color="secondary"
              />
            ))}
          </Box>
        </Box>

        <Box
          mt={2}
          display="flex"
          justifyContent="center"
          gap={1}
          flexWrap="wrap"
        >
          <Button
            variant="contained"
            color="primary"
            onClick={() => handleRoomNavigation(hotel.hotelId)}
          >
            See Rooms
          </Button>

          {amenitiesArray.length > 5 && (
            <Box textAlign="center" justifyContent={"center"}>
              <Button
                size="small"
                variant="text"
                onClick={() => setShowAllAmenities((prev) => !prev)}
              >
                {showAllAmenities ? "Ver menos" : "Ver más"}
              </Button>
            </Box>
          )}

          {roleReference?.role === "OWNER" && (
            <>
              <Button
                variant="contained"
                color="success"
                onClick={() => handleUpdateHotel(hotel.hotelId)}
              >
                Update
              </Button>

              <Button
                variant="contained"
                color="secondary"
                onClick={() => handleDeleteHotel(hotel.hotelId)}
              >
                Delete
              </Button>
            </>
          )}
        </Box>
      </CardContent>
    </Card>
  );
};

export default HotelCard;
