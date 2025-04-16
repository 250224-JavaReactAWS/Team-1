import React, { useState } from 'react';
import axios from 'axios';
import {
  TextField,
  Button,
  Container,
  Typography,
  Box,
  Paper,
  Stack
} from '@mui/material';


const RegisterHotel: React.FC = () => {
  const [formData, setFormData] = useState({
    name: '',
    location: '',
    description: '',
    priceRange: '',
    amenities: '',
    images: '',
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const payload = {
      ...formData,
      amenities: formData.amenities.split(',').map(item => item.trim()),
      images: formData.images.split(',').map(item => item.trim()),
    };

    try {
      const response = await axios.post('http://localhost:8080/hotels/register', payload,{withCredentials: true});
      alert('Hotel registration successfully');
      console.log('Response:', response.data);
    } catch (error) {
      console.error('Error at registering the hotel', error);
      alert('There was an error while registering the hotel');
    }
  };

  return (
    <Container maxWidth="sm">
      <Paper elevation={3} sx={{ p: 4, mt: 4 }}>
        <Typography variant="h5" gutterBottom>
          Registrar Hotel
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
              label="Price range(ex: 2000 - 7500)"
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
            <Button
              type="submit"
              variant="contained"
              color="primary"
              fullWidth
            >
              Enviar
            </Button>
          </Stack>
        </Box>
      </Paper>
    </Container>
  );
};

export default RegisterHotel;