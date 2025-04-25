import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {
  Container,
  Typography,
  Box,
  Button,
  TextField,
  CircularProgress,
  Paper,
  Alert,
} from "@mui/material";
import axios from "axios";

interface Hotel {
  hotelId: number;
  name: string;
  location: string;
  description: string;
  amenities: string;
  priceRange: string;
  images: string[];
}

const DeleteHotel: React.FC = () => {
  const { hotelId } = useParams<{ hotelId: string }>();
  const navigate = useNavigate();
  const [hotel, setHotel] = useState<Hotel | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [confirmationInput, setConfirmationInput] = useState("");
   const [permissionChecked, setPermissionChecked] = useState<boolean>(false);

  useEffect(() => {
    const checkPermissions = async () => {
      try {
        const response = await axios.get(
          `http://52.90.96.54:8080/hotels/${hotelId}/permissions`,
          { withCredentials: true }
        );

        if (!response.data) {
          // If the user is not the owner, redirect to the owner's hotels page
          alert("You do not have access to this page. Redirecting...");
          navigate("/ownerHotels");
        }
      } catch (error: any) {
        console.error("Error checking permissions:", error);

        // Handle 403 Unauthorized errors
        if (error.response?.status === 403) {
          alert("You are not logged in. Redirecting to the main page...");
          navigate("/ownerHotels"); // Redirect to the main page
        } else {
          alert("An error occurred. Redirecting to the main page...");
          navigate("/ownerHotels"); // Redirect to the main page
        }
        setPermissionChecked(true)
      }
    };

    const fetchHotel = async () => {
    if (permissionChecked) {
      try {
        const response = await axios.get(
        `http://52.90.96.54:8080/hotels/${hotelId}`,
        { withCredentials: true }
        );
        setHotel(response.data);
      } catch (error) {
        console.error("Error fetching hotel data:", error);
        setError("Failed to load hotel data.");
      } finally {
        setLoading(false);
      }
    }
    };

    checkPermissions();
    fetchHotel();
  }, [hotelId, navigate]);

  const handleDelete = async () => {
    if (!hotel) return;

    if (confirmationInput !== `Confirm delete ${hotel.name}`) {
      alert(
        `Please type "Confirm delete ${hotel.name}" exactly to confirm deletion.`
      );
      return;
    }

    try {
      await axios.delete(`http://52.90.96.54:8080/hotels/delete/${hotelId}`, {
        withCredentials: true,
      });
      alert("Hotel deleted successfully.");
      navigate("/ownerHotels"); // Redirect to the owner's hotels page
    } catch (error) {
      console.error("Error deleting hotel:", error);
      alert("Failed to delete the hotel. Please try again.");
    }
  };

  if (loading) {
    return (
      <Container maxWidth="sm">
        <Box display="flex" justifyContent="center" alignItems="center" height="100vh">
          <CircularProgress />
        </Box>
      </Container>
    );
  }

  if (error || !hotel) {
    return (
      <Container maxWidth="sm">
        <Alert severity="error">{error || "Hotel not found."}</Alert>
      </Container>
    );
  }

  return (
    <Container maxWidth="sm">
      <Paper elevation={3} sx={{ padding: 4, marginTop: 4 }}>
        <Typography variant="h5" gutterBottom>
          Delete Hotel
        </Typography>
        <Typography variant="body1" gutterBottom>
          Are you sure you want to delete the following hotel?
        </Typography>
        <Box mb={2}>
          <Typography variant="h6">{hotel.name}</Typography>
          <Typography variant="body2" color="text.secondary">
            {hotel.description}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Location: {hotel.location}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Price Range: {hotel.priceRange}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Amenities: {hotel.amenities}
          </Typography>
        </Box>
        <Typography variant="body2" color="error" gutterBottom>
          To confirm deletion, type: "Confirm delete {hotel.name}"
        </Typography>
        <TextField
          fullWidth
          variant="outlined"
          placeholder={`Confirm delete ${hotel.name}`}
          value={confirmationInput}
          onChange={(e) => setConfirmationInput(e.target.value)}
          sx={{ mb: 2 }}
        />
        <Box display="flex" justifyContent="space-between">
          <Button
            variant="contained"
            color="primary"
            onClick={() => navigate("/ownerHotels")}
          >
            Cancel
          </Button>
          <Button
            variant="contained"
            color="error"
            onClick={handleDelete}
          >
            Delete
          </Button>
        </Box>
      </Paper>
    </Container>
  );
};

export default DeleteHotel;