import { Alert, CircularProgress, Container, Typography } from '@mui/material';
import axios from 'axios';
import React, { useEffect, useState } from 'react';
import BookingList from './BookingList';

const BookingPage: React.FC = () => {
    const [bookings, setBookings] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchBookings = async () => {
            try {
                const response = await axios.get('http://localhost:8080/bookings/user', 
                    { withCredentials: true }
                );
                setBookings(response.data);               
            }catch (err){
                setError('Error while fetching bookings');
                console.error(err);
            } finally{
                setLoading(false);
            }
        };
        fetchBookings();
    }, []);
    
    return (
        <Container sx={{ mt: 4 }}>
            <Typography variant="h4" gutterBottom>
            Bookings List
            </Typography>
    
            {loading && <CircularProgress />}
            {error && <Alert severity="error">{error}</Alert>}
            {!loading && !error && <BookingList bookings={bookings} isLoggedIn={false} />}
        </Container>
    );
};

export default BookingPage;