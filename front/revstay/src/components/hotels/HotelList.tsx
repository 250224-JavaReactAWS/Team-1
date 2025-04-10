import { useContext, useEffect, useState } from "react";
import {
  Card,
  CardContent,
  Typography,
  Grid,
  Box,
  Chip,
  Avatar,
  CardHeader,
  IconButton,
  Button,
} from "@mui/material";
import FavoriteIcon from "@mui/icons-material/Favorite";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { authContext } from "../../App";

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
  isLoggedIn: boolean; // Nuevo prop para verificar si el usuario está logueado
}

const HotelList: React.FC<Props> = ({ hotels}) => {
  const [favoriteHotels, setFavoriteHotels] = useState<number[]>([]);
  const navigate = useNavigate();
  const roleReference = useContext(authContext);
  const isLoggedIn = roleReference?.role !== "UNAUTHENTICATED";
  useEffect(() => {
    const fetchFavorites = async () => {
      try {

        console.log(isLoggedIn)
        if (isLoggedIn) {
          const response = await axios.get<Hotel[]>(
            "http://localhost:8080/hotels/favoritesUser",
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

  const handleRoomNavigation = (hotelId: number) => {
    navigate(`/rooms/hotel/${hotelId}`);
  };

  const toggleFavorite = async (hotelId: number) => {
    try {
      if (favoriteHotels.includes(hotelId)) {
        // Si el hotel ya está en favoritos, realiza una solicitud DELETE
        await axios.delete(
          `http://localhost:8080/users/favorites/${hotelId}`,
          { withCredentials: true }
        );
  
        // Elimina el hotel de la lista de favoritos
        setFavoriteHotels((prevFavorites) =>
          prevFavorites.filter((id) => id !== hotelId)
        );
      } else {
        // Si el hotel no está en favoritos, realiza una solicitud POST
        await axios.post(
          `http://localhost:8080/users/favorites/${hotelId}`,
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
      <Grid container spacing={3}>
        {hotels.map((hotel) => (
          <Grid item xs={12} sm={6} md={4} key={hotel.hotelId}>
            <Card elevation={3}>
              <CardHeader
                avatar={<Avatar>{hotel.ownerFullName[0]}</Avatar>}
                title={hotel.name}
                subheader={hotel.location}
                action={
                  isLoggedIn && (
                    <IconButton onClick={() => toggleFavorite(hotel.hotelId)}>
                      {favoriteHotels.includes(hotel.hotelId) ? (
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
                  Precio: {hotel.priceRange}
                </Typography>
                <Box mt={2} mb={1}>
                  <Typography variant="caption">Amenidades:</Typography>
                  <Box display="flex" flexWrap="wrap" gap={1} mt={0.5}>
                    {hotel.amenities
                      .replace(/[{}]/g, "")
                      .split(",")
                      .map((amenity, index) => (
                        <Chip
                          key={index}
                          label={amenity.trim()}
                          size="small"
                          color="secondary"
                        />
                      ))}
                  </Box>
                </Box>
                <Typography variant="caption" color="text.secondary">
                  Propietario: {hotel.ownerFullName} ({hotel.ownerEmail})
                </Typography>
                <Box mt={2}>
                  <Button
                    variant="contained"
                    color="primary"
                    onClick={() => handleRoomNavigation(hotel.hotelId)}
                  >
                    Ver habitaciones
                  </Button>
                </Box>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
};

export default HotelList;