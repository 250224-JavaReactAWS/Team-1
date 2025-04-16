import { useContext, useState, useEffect } from "react";
import { Grid, Box } from "@mui/material";
import axios from "axios";
import { authContext } from "../../../App";
import HotelCard from "./HotelCard";


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

const HotelList: React.FC<Props> = ({ hotels}) => {
  const [favoriteHotels, setFavoriteHotels] = useState<number[]>([]);
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
      <Grid container spacing={3}  justifyContent="center">
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