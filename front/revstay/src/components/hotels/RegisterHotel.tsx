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
      alert('Hotel registrado con éxito!');
      console.log('Respuesta:', response.data);
    } catch (error) {
      console.error('Error al registrar el hotel:', error);
      alert('Hubo un error al registrar el hotel.');
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
              label="Nombre del hotel"
              name="name"
              value={formData.name}
              onChange={handleChange}
              fullWidth
              required
            />
            <TextField
              label="Ubicación"
              name="location"
              value={formData.location}
              onChange={handleChange}
              fullWidth
              required
            />
            <TextField
              label="Descripción"
              name="description"
              value={formData.description}
              onChange={handleChange}
              fullWidth
              multiline
              rows={3}
              required
            />
            <TextField
              label="Rango de precios (ej: 2000 - 7500)"
              name="priceRange"
              value={formData.priceRange}
              onChange={handleChange}
              fullWidth
              required
            />
            <TextField
              label="Amenidades (separadas por coma)"
              name="amenities"
              value={formData.amenities}
              onChange={handleChange}
              fullWidth
              required
            />
            <TextField
              label="URLs de imágenes (separadas por coma)"
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