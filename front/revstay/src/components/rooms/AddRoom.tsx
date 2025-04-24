import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate, useLocation } from "react-router-dom";
import {
  Box,
  Button,
  MenuItem,
  Select,
  TextField,
  Typography,
  FormControl,
  InputLabel,
} from "@mui/material";

const AddRoom: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const preselectedHotelId = location.state?.hotelId || null;

  const [isOwner, setIsOwner] = useState(false);
  const [roomData, setRoomData] = useState({
    roomType: "",
    description: "",
    price: 0,
    maxGuests: 0,
  });
  const [errors, setErrors] = useState({
    roomType: "",
    description: "",
    price: "",
    maxGuests: "",
  });
  const [hotelId, setHotelId] = useState<number | null>(preselectedHotelId);
  const [ownedHotels, setOwnedHotels] = useState<{ hotelId: number; name: string }[]>([]);

  useEffect(() => {
    axios
      .get("http://localhost:8080/hotels/owner", { withCredentials: true })
      .then((response) => {
        if (response.data.length > 0) {
          setIsOwner(true);
          setOwnedHotels(response.data);

          // If no hotelId is preselected, default to the first hotel
          if (!preselectedHotelId) {
            setHotelId(response.data[0].hotelId);
          }
        } else {
          alert("You are not authorized to add rooms to this hotel.");
          navigate("/");
        }
      })
      .catch((error) => {
        console.error("Error verifying ownership:", error);
        navigate("/");
      });
  }, [navigate, preselectedHotelId]);

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
      newErrors.roomType = "Room name is required.";
    }
    if (!roomData.description.trim()) {
      newErrors.description = "Description is required.";
    }
    if (roomData.price <= 0) {
      newErrors.price = "Price must be greater than 0.";
    }
    if (roomData.maxGuests <= 0) {
      newErrors.maxGuests = "Capacity must be greater than 0.";
    }

    setErrors(newErrors);

    // Return true if no errors
    return Object.values(newErrors).every((error) => error === "");
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    if (!validateForm()) {
      return; // Stop submission if validation fails
    }

    if (!hotelId) {
      alert("Hotel ID is missing.");
      return;
    }

    // Construct the payload with the correct structure
    const payload = {
      price: roomData.price,
      maxGuests: roomData.maxGuests,
      description: roomData.description,
      roomType: roomData.roomType,
      hotel: {
        hotelId: hotelId, // Include the hotel object with hotelId
      },
    };

    console.log("Payload:", payload); // Debugging

    axios
      .post(`http://localhost:8080/rooms`, payload, { withCredentials: true })
      .then((response) => {
        alert("Room added successfully!");
        navigate(`/hotel/${hotelId}/rooms`);
      })
      .catch((error) => {
        console.error("Error adding room:", error);
        alert("Failed to add room. Please try again.");
      });
  };

  if (!isOwner) {
    return <Typography variant="h6">Loading...</Typography>;
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
        Add Room
      </Typography>
      <form onSubmit={handleSubmit}>
        <FormControl fullWidth margin="normal">
          <InputLabel id="hotel-select-label">Select Hotel</InputLabel>
          <Select
            labelId="hotel-select-label"
            id="hotel"
            value={hotelId || ""}
            onChange={(e) => setHotelId(Number(e.target.value))}
          >
            {ownedHotels.map((hotel) => (
              <MenuItem key={hotel.hotelId} value={hotel.hotelId}>
                {hotel.name}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
        <TextField
          fullWidth
          margin="normal"
          label="Room Type"
          name="roomType"
          value={roomData.roomType}
          onChange={handleInputChange}
          error={!!errors.roomType}
          helperText={errors.roomType}
        />
        <TextField
          fullWidth
          margin="normal"
          label="Description"
          name="description"
          value={roomData.description}
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
          value={roomData.price}
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
          value={roomData.maxGuests}
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
          Add Room
        </Button>
      </form>
    </Box>
  );
};

export default AddRoom;