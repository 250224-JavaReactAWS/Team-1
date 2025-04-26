import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate, useLocation } from "react-router-dom";
import { Box, Button, TextField, Typography } from "@mui/material";

/**
 * `EditRoom` is a React functional component for editing the details of a room.
 * 
 * This component allows hotel owners to update room details such as room type, 
 * description, price, and maximum guest capacity. It ensures that the user has 
 * the necessary permissions before displaying the form.
 * 
 * State:
 * - `roomData` (object): Stores the current room details being edited.
 *   - `roomType` (string): The type of the room (e.g., Single, Double).
 *   - `description` (string): A brief description of the room.
 *   - `price` (number): The price of the room.
 *   - `maxGuests` (number): The maximum number of guests allowed in the room.
 * - `errors` (object): Stores validation error messages for the form fields.
 * - `permissionChecked` (boolean): Indicates whether the user's permissions have been verified.
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
 * - `checkPermissions`: Ensures the user has the necessary permissions to edit the room.
 * - `handleInputChange`: Updates the `roomData` state when the user types in the form fields.
 * - `validateForm`: Validates the form fields and sets error messages if validation fails.
 * - `handleUpdateRoom`: Handles the form submission, validates the input, and sends the updated data to the backend API.
 * 
 * API Endpoints:
 * - GET `http://52.90.96.54:8080/hotels/{hotelId}/permissions`: Checks if the user has permissions to edit the room.
 * - PUT `http://52.90.96.54:8080/rooms/{roomId}`: Updates the room details.
 * 
 * UI Elements:
 * - `Box`: A Material-UI container for layout and styling.
 * - `Typography`: Displays the form title and loading messages.
 * - `TextField`: Input fields for room details such as type, description, price, and capacity.
 * - `Button`: A button to submit the form.
 * 
 * Behavior:
 * - If the user does not have permissions, they are redirected to the previous page.
 * - The user must fill in all required fields before submitting the form.
 * - If the room is updated successfully, the user is redirected to the hotel's room list page.
 * - If the update fails, an error message is displayed.
 * 
 * Example Usage:
 * ```tsx
 * <EditRoom />
 * ```
 */

const EditRoom: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const roomFromState = location.state?.room;

  //   console.log("Room from state:", roomFromState); // Debugging

  const [roomData, setRoomData] = useState({
    roomType: roomFromState?.roomType || "",
    description: roomFromState?.description || "",
    price: roomFromState?.price || 0,
    maxGuests: roomFromState?.maxGuests || 0,
  });

  const [errors, setErrors] = useState({
    roomType: "",
    description: "",
    price: "",
    maxGuests: "",
  });

  const [permissionChecked, setPermissionChecked] = useState(false);

  useEffect(() => {
    const checkPermissions = async () => {
      try {
        // Check if the user has permission to edit the room's hotel
        const response = await axios.get(
          `http://52.90.96.54:8080/hotels/${roomFromState?.hotelId}/permissions`,
          { withCredentials: true }
        );

        if (!response.data) {
          alert(
            "You do not have access to edit this room. Redirecting to the previous page."
          );
          navigate(-1); // Redirect to the previous page
        } else {
          setPermissionChecked(true); // User has permission
        }
      } catch (error) {
        console.error("Error checking permissions:", error);
        alert(
          "An error occurred while checking permissions. Redirecting to the previous page."
        );
        navigate(-1); // Redirect to the previous page
      }
    };

    if (roomFromState?.hotelId) {
      checkPermissions();
    } else {
      alert("Invalid room data. Redirecting to the previous page.");
      navigate(-1); // Redirect to the previous page
    }
  }, [roomFromState, navigate]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setRoomData((prevData) => ({
      ...prevData,
      [name]: name === "price" || name === "maxGuests" ? Number(value) : value,
    }));
  };

  const validateForm = () => {
    const newErrors = {
      roomType: "",
      description: "",
      price: "",
      maxGuests: "",
    };

    if (!roomData.roomType.trim()) {
      newErrors.roomType = "Room type is required.";
    }
    if (!roomData.description.trim()) {
      newErrors.description = "Description is required.";
    }
    if (roomData.price <= 0) {
      newErrors.price = "Price must be greater than 0.";
    }
    if (roomData.maxGuests <= 0) {
      newErrors.maxGuests = "Maximum guests must be greater than 0.";
    }

    setErrors(newErrors);

    // Return true if no errors
    return Object.values(newErrors).every((error) => error === "");
  };

  const handleUpdateRoom = (e: React.FormEvent) => {
    e.preventDefault();

    if (!validateForm()) {
      return; // Stop submission if validation fails
    }

    axios
      .put(`http://52.90.96.54:8080/rooms/${roomFromState.roomId}`, roomData, {
        withCredentials: true,
      })
      .then(() => {
        alert("Room updated successfully!");
        navigate(`/rooms/hotel/${roomFromState.hotelId}`);
      })
      .catch((error) => {
        console.error("Error updating room:", error);
        alert("Failed to update room. Please try again.");
      });
  };

  if (!permissionChecked) {
    return (
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          height: "100vh",
        }}
      >
        <Typography variant="h6">Checking permissions...</Typography>
      </Box>
    );
  }

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
        Edit Room
      </Typography>
      <form onSubmit={handleUpdateRoom}>
        <TextField
          fullWidth
          margin="normal"
          label="Room Type"
          name="roomType"
          value={roomData.roomType || ""}
          onChange={handleInputChange}
          error={!!errors.roomType}
          helperText={errors.roomType}
        />
        <TextField
          fullWidth
          margin="normal"
          label="Description"
          name="description"
          value={roomData.description || ""}
          onChange={handleInputChange}
          error={!!errors.description}
          helperText={errors.description}
        />
        <TextField
          fullWidth
          margin="normal"
          label="Price"
          name="price"
          type="number"
          value={roomData.price || 0}
          onChange={handleInputChange}
          error={!!errors.price}
          helperText={errors.price}
        />
        <TextField
          fullWidth
          margin="normal"
          label="Maximum Guests"
          name="maxGuests"
          type="number"
          value={roomData.maxGuests || 0}
          onChange={handleInputChange}
          error={!!errors.maxGuests}
          helperText={errors.maxGuests}
        />
        <Button
          type="submit"
          variant="contained"
          color="primary"
          fullWidth
          sx={{ marginTop: 2 }}
        >
          Update Room
        </Button>
      </form>
    </Box>
  );
};

export default EditRoom;
