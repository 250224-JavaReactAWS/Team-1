import React, { useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router';
import { authContext } from '../../App';
import axios from 'axios';
import { Box, Button, Card, CardContent, CardHeader, Grid, Typography } from '@mui/material';

interface Booking {
    bookingId: number;
    userEmail: string;
    roomId: number;
    roomType: string;
    hotelId: number;
    hotelName: string;
    checkIn: string;
    checkOut: string;
    guests: number;
    status: string;
}

interface BookingListProps {
    bookings: Booking[];
    isLoggedIn: boolean;
}

const BookingList: React.FC<BookingListProps> = ({ bookings }) => {
    const [bookingsList, setBookings] = useState<number[]>([]);
    const navigate = useNavigate();
    const roleReference = useContext(authContext);
    const isLoggedIn = roleReference?.role !== "UNAUTHENTICATED";

    useEffect(() => {
        const fetchBookings = async () => {
            try {
                if (isLoggedIn) {
                    const response = await axios.get<Booking[]>(
                        "http://localhost:8080/bookings/user",
                        { withCredentials: true }
                    );
                    const bookingIds = response.data.map((booking)=> booking.bookingId)
                    setBookings(bookingIds);
                }
            } catch (error) {
                console.error("Error fetching bookings:", error);
            }
        };

        fetchBookings();
    }, [isLoggedIn]);

    return (
        <Box p={4}>
            <Grid container spacing={3}>
                {bookings.map((booking) => (
                    <Grid item xs={12} sm={6} md={4} key={booking.bookingId}>
                        <Card elevation={3}>
                            <CardHeader
                                title={`Booking ID: ${booking.bookingId}`}
                                subheader={`Hotel: ${booking.hotelName}`}
                            />
                            <CardContent>
                                <Typography variant="body2" color="text.secondary" gutterBottom>
                                    Room Type: {booking.roomType}
                                </Typography>
                                <Typography variant="body2" color="text.secondary" gutterBottom>
                                    Check-In: {new Date(booking.checkIn).toLocaleDateString()}
                                </Typography>
                                <Typography variant="body2" color="text.secondary" gutterBottom>
                                    Check-Out: {new Date(booking.checkOut).toLocaleDateString()}
                                </Typography>
                                <Typography variant="body2" color="text.secondary" gutterBottom>
                                    Guests: {booking.guests}
                                </Typography>
                                <Typography variant="body2" color="text.secondary" gutterBottom>
                                    Status: {booking.status}
                                </Typography>
                                <Box mt={2}>
                                    <Button
                                        variant="contained"
                                        color="primary"
                                        onClick={() => navigate(`/bookings/${booking.bookingId}`)}
                                    >
                                        View Details
                                    </Button>
                                </Box>
                            </CardContent>
                        </Card>
                    </Grid>
                ))}
            </Grid>
        </Box>
    );
};

export default BookingList;
