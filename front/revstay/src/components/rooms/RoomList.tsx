import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";
import { Box, Card, CardContent, Typography, Grid, Button } from "@mui/material";

// Define el tipo de habitación
type Room = {
  roomId: number;
  hotelId: number;
  hotelName: string;
  roomType: string;
  description: string | null;
  price: number;
  maxGuests: number;
};

function RoomList() {
  const { hotelId } = useParams<{ hotelId: string }>(); // Obtiene el hotelId desde la URL
  const hotelIdNumber = Number(hotelId); // Convierte el hotelId a número
  const [rooms, setRooms] = useState<Room[]>([]);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate()

  useEffect(() => {
    const fetchRooms = async () => {
      try {
        const response = await axios.get<Room[]>(
          `http://localhost:8080/rooms/hotel/${hotelId}`,
          { withCredentials: true }
        );
        setRooms(response.data); // Actualiza el estado con las habitaciones obtenidas
        // console.log("Hotel Name:", response.data[0]?.hotelName || "No hotel name available");
      } catch (err) {
        console.error("Error fetching rooms:", err);
        setError("Not able to load rooms");
      }
    };

    fetchRooms();
  }, [hotelId]);

  const handleRoomSelect = (room: Room) => {
    console.log(room)
    navigate('/bookings/reserve', { state: {room} });
  };

  return (
    <Box p={4}>
      <Typography variant="h4" gutterBottom>
        {rooms.length > 0 ? `${rooms[0].hotelName} Rooms` : `Rooms ${hotelId}`}
      </Typography>
      {error && (
        <Typography color="error" gutterBottom>
          {error}
        </Typography>
      )}
      <Grid container spacing={3}>
        {rooms.map((room) => (
          <Grid item xs={12} key={room.roomId}>
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
              <CardContent>
                <Button variant="contained" onClick={() => handleRoomSelect(room)}>
                  Book
                </Button>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
}

export default RoomList;