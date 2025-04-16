import { useNavigate } from 'react-router';
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
    const navigate = useNavigate();

    return (
        <Box p={4}>
            <Grid container spacing={3}>
                {bookings.map((booking) => (
                    <Grid size={6} key={booking.bookingId}>
                        <Card elevation={3}>
                            <CardHeader
                                title={booking.hotelName}
                                subheader={`Booking ID: ${booking.bookingId}`}
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
