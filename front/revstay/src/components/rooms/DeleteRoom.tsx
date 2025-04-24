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
      .delete(`http://localhost:8080/rooms/${room.roomId}`, { withCredentials: true })
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
        To confirm deletion, please type <strong>"DELETE"</strong> in the box below:
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
      <Typography variant="body2" color="text.secondary" align="center" sx={{ marginTop: 2 }}>
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