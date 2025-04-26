/**
 * `RegisterHotel` is a React functional component for registering a new hotel.
 * 
 * This component provides a form for users to input hotel details such as name, location, 
 * description, price range, amenities, and image URLs. The form data is sent to the backend API 
 * for hotel registration.
 * 
 * State:
 * - `formData` (object): Stores the form input values.
 *   - `name` (string): The name of the hotel.
 *   - `location` (string): The location of the hotel.
 *   - `description` (string): A brief description of the hotel.
 *   - `priceRange` (string): The price range of the hotel.
 *   - `amenities` (string): A comma-separated list of amenities offered by the hotel.
 *   - `images` (string): A comma-separated list of image URLs for the hotel.
 * 
 * Methods:
 * - `handleChange`: Updates the `formData` state when the user types in the form fields.
 * - `handleSubmit`: Handles the form submission, processes the input data, and sends it to the backend API.
 * 
 * API Endpoint:
 * - POST `http://52.90.96.54:8080/hotels/register`: Registers a new hotel with the provided details.
 * 
 * UI Elements:
 * - `Container`: A Material-UI container for layout and spacing.
 * - `Paper`: A Material-UI paper component for styling the form.
 * - `Typography`: Displays the form title.
 * - `TextField`: Input fields for hotel details such as name, location, description, etc.
 * - `Button`: A button to submit the form.
 * - `Stack`: A Material-UI stack for spacing between form elements.
 * 
 * Behavior:
 * - The user must fill in all required fields before submitting the form.
 * - The `amenities` and `images` fields accept comma-separated values, which are processed into arrays before submission.
 * - If the registration is successful, a success message is displayed. Otherwise, an error message is shown.
 * 
 * Example Usage:
 * ```tsx
 * <RegisterHotel />
 * ```
 */

import React, { useState } from "react";
import axios from "axios";
import {
  TextField,
  Button,
  Container,
  Typography,
  Box,
  Paper,
  Stack,
} from "@mui/material";

const RegisterHotel: React.FC = () => {
  const [formData, setFormData] = useState({
    name: "",
    location: "",
    description: "",
    priceRange: "",
    amenities: "",
    images: "",
  });

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const payload = {
      ...formData,
      amenities: formData.amenities.split(",").map((item) => item.trim()),
      images: formData.images.split(",").map((item) => item.trim()),
    };

    try {
      const response = await axios.post(
        "http://52.90.96.54:8080/hotels/register",
        payload,
        { withCredentials: true }
      );
      alert("Hotel registration successfully");
      console.log("Response:", response.data);
    } catch (error) {
      console.error("Error at registering the hotel", error);
      alert("There was an error while registering the hotel");
    }
  };

  return (
    <Container maxWidth="sm">
      <Paper elevation={3} sx={{ p: 4, mt: 4 }}>
        <Typography variant="h5" gutterBottom>
          Register Hotel
        </Typography>
        <Box component="form" onSubmit={handleSubmit}>
          <Stack spacing={2}>
            <TextField
              label="Hotel Name"
              name="name"
              value={formData.name}
              onChange={handleChange}
              fullWidth
              required
            />
            <TextField
              label="Location"
              name="location"
              value={formData.location}
              onChange={handleChange}
              fullWidth
              required
            />
            <TextField
              label="Description"
              name="description"
              value={formData.description}
              onChange={handleChange}
              fullWidth
              multiline
              rows={3}
              required
            />
            <TextField
              label="Price range (e.g., 2000 - 7500)"
              name="priceRange"
              value={formData.priceRange}
              onChange={handleChange}
              fullWidth
              required
            />
            <TextField
              label="Amenities (separated by a comma)"
              name="amenities"
              value={formData.amenities}
              onChange={handleChange}
              fullWidth
              required
            />
            <TextField
              label="URLs of the images (separated by a comma)"
              name="images"
              value={formData.images}
              onChange={handleChange}
              fullWidth
              required
            />
            <Button type="submit" variant="contained" color="primary" fullWidth>
              Submit
            </Button>
          </Stack>
        </Box>
      </Paper>
    </Container>
  );
};

export default RegisterHotel;
