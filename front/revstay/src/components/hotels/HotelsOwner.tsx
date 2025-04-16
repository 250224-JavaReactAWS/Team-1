// HotelPage.tsx
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { CircularProgress, Container, Typography, Alert } from '@mui/material';
import HotelList from './HotelList';

const HotelPage: React.FC = () => {
  const [hotels, setHotels] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchHotels = async () => {
      try {
        const response = await axios.get('http://localhost:8080/hotels/owner', { withCredentials: true });
        setHotels(response.data);
      } catch (err) {
        setError('Error at obtaining hotels');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchHotels();
  }, []);




  return (
    <Container /*sx={{ mt: 4 }}*/>
      <Typography variant="h4" gutterBottom>
        Owner Hotels
      </Typography>
      {loading && <CircularProgress />}
      {error && <Alert severity="error">{error}</Alert>}
      {!loading && !error && <HotelList hotels={hotels} />}
    </Container>
  );
};

export default HotelPage;