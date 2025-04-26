import { useEffect, useState, useContext } from "react";

/**
 * `RoomList` is a React functional component for displaying a list of rooms in a hotel.
 * 
 * This component fetches room data from the backend API and displays it in a card format. 
 * It provides options for booking, editing, or deleting rooms based on the user's role.
 * 
 * State:
 * - `rooms` (Room[]): An array of room objects fetched from the API.
 * - `error` (string | null): Stores any error message encountered during data fetching.
 * 
 * Context:
 * - `authContext`: Provides the user's role to determine access to certain actions.
 * 
 * Room Object Structure:
 * - `roomId` (number): The unique identifier for the room.
 * - `roomType` (string): The type of the room (e.g., Single, Double).
 * - `description` (string | undefined): A brief description of the room.
 * - `price` (number): The price of the room.
 * - `maxGuests` (number): The maximum number of guests allowed in the room.
 * - `hotelName` (string): The name of the hotel to which the room belongs.
 * 
 * Methods:
 * - `fetchRooms`: Fetches the list of rooms for the specified hotel from the backend API.
 * - `handleRoomSelect`: Navigates to the booking page for the selected room.
 * - `handleAddRoom`: Navigates to the "Add Room" page for the hotel.
 * - `handleEditRoom`: Navigates to the "Edit Room" page for the selected room.
 * - `handleDeleteRoom`: Navigates to the "Delete Room" page for the selected room.
 * 
 * API Endpoints:
 * - GET `http://52.90.96.54:8080/rooms/hotel/{hotelId}`: Fetches the list of rooms for the specified hotel.
 * 
 * UI Elements:
 * - `Box`: A Material-UI container for layout and styling.
 * - `Typography`: Displays the page title and error messages.
 * - `Grid`: A Material-UI grid layout for organizing room cards.
 * - `Card`: Displays room details in a styled card format.
 * - `CardContent`: Displays room details and action buttons.
 * - `Button`: Provides actions for booking, editing, or deleting rooms.
 * 
 * Behavior:
 * - If the user is an "OWNER", they can add, edit, or delete rooms.
 * - If the user is not an "OWNER", they can only book rooms.
 * - If no rooms are available, an error message is displayed.
 * 
 * Example Usage:
 * ```tsx
 * <RoomList />
 * ```
 */
interface Room {
  roomId: number;
  roomType: string;
  description?: string;
  price: number;
  maxGuests: number;
  hotelName: string;
}
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";
import {
  Box,
  Card,
  CardContent,
  Typography,
  Grid,
  Button,
} from "@mui/material";
import { authContext } from "../../App"; // Assuming authContext is defined in App.tsx

function RoomList() {
  const { hotelId } = useParams<{ hotelId: string }>();
  const hotelIdNumber = Number(hotelId);
  const [rooms, setRooms] = useState<Room[]>([]);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();
  const auth = useContext(authContext); // Access the user's role from the context

  useEffect(() => {
    const fetchRooms = async () => {
      try {
        const response = await axios.get<Room[]>(
          `http://52.90.96.54:8080/rooms/hotel/${hotelId}`,
          { withCredentials: true }
        );
        setRooms(response.data);
      } catch (err) {
        console.error("Error fetching rooms:", err);
        setError("Not able to load rooms");
      }
    };

    fetchRooms();
  }, [hotelId]);

  const handleRoomSelect = (room: Room) => {
    navigate("/bookings/reserve", { state: { room } });
  };

  const handleAddRoom = () => {
    navigate(`/rooms/add/${hotelId}`, { state: { hotelId: hotelIdNumber } });
  };

  const handleEditRoom = (room: Room) => {
    navigate(`/rooms/edit/${room.roomId}`, { state: { room } });
  };

  const handleDeleteRoom = (room: Room) => {
    navigate(`/rooms/delete/${room.roomId}`, { state: { room } });
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
      {/* Show "Add Room" button only if the user is the owner */}
      {auth?.role === "OWNER" && (
        <Button
          variant="contained"
          onClick={handleAddRoom}
          sx={{ marginBottom: 2 }}
        >
          Add Room
        </Button>
      )}
      <Grid container spacing={3}>
        {rooms.map((room) => (
          <Grid key={room.roomId}>
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
              {/* Show "Edit" and "Delete" buttons only if the user is the owner */}
              {auth?.role === "OWNER" && (
                <CardContent>
                  <Button
                    variant="outlined"
                    onClick={() => handleEditRoom(room)}
                    sx={{ marginRight: 1 }}
                  >
                    Edit
                  </Button>
                  <Button
                    variant="outlined"
                    color="error"
                    onClick={() => handleDeleteRoom(room)}
                  >
                    Delete
                  </Button>
                </CardContent>
              )}
              {/* Show "Book" button for all users */}
              {auth?.role !== "OWNER" && (
                <CardContent>
                  <Button
                    variant="contained"
                    onClick={() => handleRoomSelect(room)}
                  >
                    Book
                  </Button>
                </CardContent>
              )}
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
}

export default RoomList;
