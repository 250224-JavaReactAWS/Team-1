import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import {
  Box,
  Button,
  Typography,
  TextField,
  Card,
  CardContent,
} from "@mui/material";
import axios from "axios";

/**
 * `DeleteRoom` is a React functional component for deleting a room.
 * 
 * This component allows hotel owners to delete a specific room after confirming their intent. 
 * It ensures that the user provides explicit confirmation before proceeding with the deletion.
 * 
 * State:
 * - `confirmationText` (string): Stores the user's input for confirming the deletion.
 * 
 * Props:
 * - None.
 * 
 * Room Object Structure (passed via `location.state`):
 * - `roomId` (number): The unique identifier for the room.
 * - `roomType` (string): The type of the room (e.g., Single, Double).
 * - `description` (string): A brief description of the room.
 * - `price` (number): The price of the room.
 * - `maxGuests` (number): The maximum number of guests allowed in the room.
 * - `hotelId` (number): The ID of the hotel to which the room belongs.
 * 
 * Methods:
 * - `handleDeleteRoom`: Deletes the room after validating the confirmation input.
 * - `handleEditRoom`: Redirects the user to the room edit page.
 * 
 * API Endpoint:
 * - DELETE `http://52.90.96.54:8080/rooms/{roomId}`: Deletes the specified room.
 * 
 * UI Elements:
 * - `Box`: A Material-UI container for layout and styling.
 * - `Typography`: Displays the page title, room details, and confirmation instructions.
 * - `Card`: Displays the room details in a styled card format.
 * - `TextField`: Input field for typing the confirmation text.
 * - `Button`: A button to confirm the deletion or navigate to the edit page.
 * 
 * Behavior:
 * - If no room data is passed via `location.state`, the user is redirected back to the previous page.
 * - The user must type "DELETE" exactly to confirm the deletion.
 * - If the deletion is successful, the user is redirected to the hotel's room list page.
 * - If the deletion fails, an error message is displayed.
 * 
 * Example Usage:
 * ```tsx
 * <DeleteRoom />
 * ```
 */
const DeleteRoom: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const room = location.state?.room; // Extract room information from state

  const [confirmationText, setConfirmationText] = useState("");

  if (!room) {
    // Redirect back if no room data is passed
    navigate(-1);
    return null;
  }

  const handleDeleteRoom = () => {
    if (confirmationText !== "DELETE") {
      alert('Please type "DELETE" to confirm.');
      return;
    }

    axios
      .delete(`http://52.90.96.54:8080/rooms/${room.roomId}`, {
        withCredentials: true,
      })
      .then(() => {
        alert("Room deleted successfully!");
        navigate(`/rooms/hotel/${room.hotelId}`); // Redirect to the room list
      })
      .catch((error) => {
        console.error("Error deleting room:", error);
        alert("Failed to delete room. Please try again.");
      });
  };

  const handleEditRoom = () => {
    navigate(`/rooms/edit/${room.roomId}`, { state: { room } });
  };

  return (
    <Box
      sx={{
        maxWidth: 600,
        margin: "auto",
        padding: 4,
        boxShadow: 3,
        borderRadius: 2,
        backgroundColor: "white",
      }}
    >
      <Typography variant="h4" gutterBottom>
        Delete Room
      </Typography>
      <Card elevation={3} sx={{ marginBottom: 2 }}>
        <CardContent>
          <Typography variant="h6">{room.roomType}</Typography>
          <Typography variant="body2" color="text.secondary" gutterBottom>
            {room.description || "No description available"}
          </Typography>
          <Typography variant="subtitle1" color="primary">
            Price: ${room.price.toFixed(2)}
          </Typography>
          <Typography variant="caption" color="text.secondary">
            Maximum Capacity: {room.maxGuests} persons
          </Typography>
        </CardContent>
      </Card>
      <Typography variant="body1" gutterBottom>
        To confirm deletion, please type <strong>"DELETE"</strong> in the box
        below:
      </Typography>
      <TextField
        fullWidth
        margin="normal"
        label="Type DELETE to confirm"
        value={confirmationText}
        onChange={(e) => setConfirmationText(e.target.value)}
      />
      <Button
        variant="contained"
        color="error"
        fullWidth
        sx={{ marginTop: 2 }}
        onClick={handleDeleteRoom}
      >
        Delete Room
      </Button>
      <Typography
        variant="body2"
        color="text.secondary"
        align="center"
        sx={{ marginTop: 2 }}
      >
        If you want to edit this room instead,{" "}
        <Button variant="text" onClick={handleEditRoom}>
          click here
        </Button>
        .
      </Typography>
    </Box>
  );
};

export default DeleteRoom;
