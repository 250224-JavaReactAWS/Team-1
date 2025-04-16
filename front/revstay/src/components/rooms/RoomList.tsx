import { useEffect, useState } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
import { Box, Card, CardContent, Typography, Grid } from "@mui/material";

// Define el tipo de habitación
type Room = {
  roomId: number;
  hotelId: number;
  roomType: string;
  description: string | null;
  price: number;
  maxGuests: number;
};

function RoomList() {
  const { hotelId } = useParams<{ hotelId: string }>(); // Obtiene el hotelId desde la URL
  const [rooms, setRooms] = useState<Room[]>([]);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchRooms = async () => {
      try {
        const response = await axios.get<Room[]>(
          `http://localhost:8080/rooms/hotel/${hotelId}`,
          { withCredentials: true }
        );
        setRooms(response.data); // Actualiza el estado con las habitaciones obtenidas
      } catch (err) {
        console.error("Error fetching rooms:", err);
        setError("No se pudieron cargar las habitaciones.");
      }
    };

    fetchRooms();
  }, [hotelId]);

  return (
    <Box p={4}>
      {/* <Typography variant="h4" gutterBottom>
      Hotel Rooms {hotelId}
      </Typography> */}
      {error && (
        <Typography color="error" gutterBottom>
          {error}
        </Typography>
      )}
      <Grid container spacing={3}>
        {rooms.map((room) => (
          <Grid size={12} key={room.roomId}>
            <Card elevation={3}>
              <CardContent>
                <Typography variant="h6">{room.roomType}</Typography>
                <Typography variant="body2" color="text.secondary" gutterBottom>
                  {room.description || "No description Available"}
                </Typography>
                <Typography variant="subtitle1" color="primary">
                  Price: ${room.price.toFixed(2)}
                </Typography>
                <Typography variant="caption" color="text.secondary">
                  Maximum Capacity: {room.maxGuests} persons
                </Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
}

export default RoomList;