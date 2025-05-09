/**
 * `UpdateHotel` is a React functional component for updating hotel details.
 * 
 * This component allows the owner of a hotel to update its details, such as name, location, 
 * description, price range, amenities, and images. It ensures that the user has the necessary 
 * permissions before displaying the form.
 * 
 * State:
 * - `formData` (Hotel | null): Stores the current hotel details fetched from the backend.
 * - `errors` (Partial<Record<keyof Hotel, string>>): Stores validation error messages for form fields.
 * - `loading` (boolean): Indicates whether the component is in a loading state.
 * - `permissionChecked` (boolean): Indicates whether the user's permissions have been verified.
 * 
 * Props:
 * - None.
 * 
 * Hotel Object Structure:
 * - `id` (number): The unique identifier for the hotel.
 * - `name` (string): The name of the hotel.
 * - `location` (string): The location of the hotel.
 * - `description` (string): A brief description of the hotel.
 * - `priceRange` (string): The price range of the hotel.
 * - `amenities` (string): A comma-separated list of amenities offered by the hotel.
 * - `images` (string): A comma-separated list of image URLs for the hotel.
 * 
 * Methods:
 * - `checkPermissions`: Ensures the user has the necessary permissions to update the hotel.
 * - `fetchHotel`: Fetches the hotel details from the backend.
 * - `validateField`: Validates individual form fields.
 * - `handleChange`: Updates the `formData` state and validates the field when the user types in the form fields.
 * - `handleSubmit`: Handles the form submission, validates all fields, and sends the updated data to the backend API.
 * 
 * API Endpoints:
 * - GET `http://52.90.96.54:8080/hotels/{hotelId}/permissions`: Checks if the user has permissions to update the hotel.
 * - GET `http://52.90.96.54:8080/hotels/{hotelId}`: Fetches the hotel details.
 * - PUT `http://52.90.96.54:8080/hotels/update/{hotelId}`: Updates the hotel details.
 * 
 * UI Elements:
 * - `Container`: A Material-UI container for layout and spacing.
 * - `Paper`: A Material-UI paper component for styling the form.
 * - `Typography`: Displays the form title and error messages.
 * - `TextField`: Input fields for hotel details such as name, location, description, etc.
 * - `Button`: A button to submit the form.
 * - `CircularProgress`: A loading spinner displayed while data is being fetched.
 * 
 * Behavior:
 * - If the user does not have permissions, they are redirected to the owner's hotels page.
 * - The user must fill in all required fields before submitting the form.
 * - The `amenities` and `images` fields accept comma-separated values, which are processed into arrays before submission.
 * - If the update is successful, a success message is displayed. Otherwise, an error message is shown.
 * 
 * Example Usage:
 * ```tsx
 * <UpdateHotel />
 * ```
 */

import React, { useState, useEffect } from "react";
import {
  TextField,
  Button,
  Box,
  Typography,
  Container,
  Paper,
  CircularProgress,
} from "@mui/material";
import { useParams, useNavigate } from "react-router";
import axios from "axios";

interface Hotel {
  id: number;
  name: string;
  location: string;
  description: string;
  priceRange: string;
  amenities: string;
  images: string;
}

const UpdateHotel: React.FC = () => {
  const { hotelId } = useParams<{ hotelId: string }>();
  const navigate = useNavigate();
  const [formData, setFormData] = useState<Hotel | null>(null);
  const [errors, setErrors] = useState<Partial<Record<keyof Hotel, string>>>(
    {}
  );
  const [loading, setLoading] = useState<boolean>(true);
  const [permissionChecked, setPermissionChecked] = useState<boolean>(false);

  useEffect(() => {
    const checkPermissions = async () => {
      try {
        const response = await axios.get(
          `http://52.90.96.54:8080/hotels/${hotelId}/permissions`,
          { withCredentials: true }
        );
        if (!response.data) {
          alert(
            "You do not have access to this page. Redirecting to the main page."
          );
          navigate("/ownerHotels"); // Redirect to the main page
        } else {
          setPermissionChecked(true); // User has permission
        }
      } catch (error) {
        console.error("Error checking permissions:", error);
        alert(
          "An error occurred while checking permissions. Redirecting to the main page."
        );
        navigate("/ownerHotels"); // Redirect to the main page
      }
    };

    checkPermissions();
  }, [hotelId, navigate]);

  useEffect(() => {
    if (permissionChecked) {
      // Fetch hotel data
      const fetchHotel = async () => {
        try {
          const response = await axios.get(
            `http://52.90.96.54:8080/hotels/${hotelId}`
          );
          setFormData(response.data); // Set the fetched hotel data
          setLoading(false);
        } catch (error) {
          console.error("Error fetching hotel data:", error);
          setLoading(false);
        }
      };

      fetchHotel();
    }
  }, [hotelId, permissionChecked]);

  const validateField = (name: keyof Hotel, value: string) => {
    if (!value.trim()) {
      return `${name} is required.`;
    }
    return "";
  };

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    if (formData) {
      setFormData({ ...formData, [name]: value });

      // Validate the field
      const error = validateField(name as keyof Hotel, value);
      setErrors({ ...errors, [name]: error });
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!formData) return;

    // Validate all fields
    const newErrors: Partial<Record<keyof Hotel, string>> = {};
    Object.keys(formData).forEach((key) => {
      const field = key as keyof Hotel;
      const error = validateField(field, String(formData[field]));
      if (error) {
        newErrors[field] = error;
      }
    });

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      alert("Please fix the errors before submitting.");
      return;
    }

    try {
      // Transform amenities and images into arrays
      const updatedFormData = {
        ...formData,
        amenities:
          typeof formData.amenities === "string"
            ? formData.amenities.split(",").map((item) => item.trim())
            : formData.amenities, // Ensure amenities is a string before splitting
        images:
          typeof formData.images === "string"
            ? formData.images.split(",").map((url) => url.trim())
            : formData.images, // Ensure images is a string before splitting
      };

      console.log("Payload being sent:", updatedFormData);

      await axios.put(
        `http://52.90.96.54:8080/hotels/update/${hotelId}`,
        updatedFormData,
        { withCredentials: true }
      );
      alert("Hotel updated successfully!");
    } catch (error) {
      if (axios.isAxiosError(error)) {
        console.error(
          "Error updating hotel:",
          error.response?.data || error.message
        );
      } else {
        console.error("Error updating hotel:", error);
      }
      alert("Failed to update hotel. Please try again.");
    }
  };

  if (!permissionChecked || loading) {
    return (
      <Container maxWidth="sm">
        <Box
          display="flex"
          justifyContent="center"
          alignItems="center"
          height="100vh"
        >
          <CircularProgress />
        </Box>
      </Container>
    );
  }

  if (!formData) {
    return (
      <Container maxWidth="sm">
        <Typography variant="h6" color="error">
          Failed to load hotel data.
        </Typography>
      </Container>
    );
  }

  return (
    <Container maxWidth="sm">
      <Paper elevation={3} sx={{ padding: 4, marginTop: 4 }}>
        <Typography variant="h5" component="h1" gutterBottom>
          Update Hotel
        </Typography>
        <form onSubmit={handleSubmit}>
          <Box display="flex" flexDirection="column" gap={2}>
            <TextField
              label="Hotel Name"
              id="name"
              name="name"
              value={formData.name}
              onChange={handleChange}
              error={!!errors.name}
              helperText={errors.name}
              required
              fullWidth
            />
            <TextField
              label="Location"
              id="location"
              name="location"
              value={formData.location}
              onChange={handleChange}
              error={!!errors.location}
              helperText={errors.location}
              required
              fullWidth
            />
            <TextField
              label="Description"
              id="description"
              name="description"
              value={formData.description}
              onChange={handleChange}
              error={!!errors.description}
              helperText={errors.description}
              required
              fullWidth
              multiline
              rows={4}
            />
            <TextField
              label="Price Range"
              id="priceRange"
              name="priceRange"
              value={formData.priceRange}
              onChange={handleChange}
              error={!!errors.priceRange}
              helperText={errors.priceRange}
              required
              fullWidth
            />
            <TextField
              label="Amenities"
              id="amenities"
              name="amenities"
              value={formData.amenities}
              onChange={handleChange}
              error={!!errors.amenities}
              helperText={
                errors.amenities ||
                "Enter amenities separated by commas (e.g., WiFi, Pool, Gym)"
              }
              required
              fullWidth
            />
            <TextField
              label="Images"
              id="images"
              name="images"
              value={formData.images}
              onChange={handleChange}
              error={!!errors.images}
              helperText={
                errors.images ||
                "Enter image URLs separated by commas (e.g., https://example.com/image1.jpg, https://example.com/image2.jpg)"
              }
              required
              fullWidth
            />
            <Button type="submit" variant="contained" color="primary" fullWidth>
              Update Hotel
            </Button>
          </Box>
        </form>
      </Paper>
    </Container>
  );
};

export default UpdateHotel;
