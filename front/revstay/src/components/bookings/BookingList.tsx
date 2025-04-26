/**
 * `BookingList` is a React functional component for displaying a list of bookings.
 *
 * This component renders a grid of booking cards, each showing details about a specific booking,
 * such as the hotel name, room type, check-in and check-out dates, number of guests, and booking status.
 *
 * Props:
 * - `bookings` (Booking[]): An array of booking objects to display.
 * - `isLoggedIn` (boolean): Indicates whether the user is logged in.
 *
 * Booking Object Structure:
 * - `bookingId` (number): The unique identifier for the booking.
 * - `userEmail` (string): The email of the user who made the booking.
 * - `roomId` (number): The ID of the room associated with the booking.
 * - `roomType` (string): The type of the room (e.g., Single, Double).
 * - `hotelId` (number): The ID of the hotel associated with the booking.
 * - `hotelName` (string): The name of the hotel.
 * - `checkIn` (string): The check-in date and time.
 * - `checkOut` (string): The check-out date and time.
 * - `guests` (number): The number of guests for the booking.
 * - `status` (string): The status of the booking (e.g., CONFIRMED, PENDING).
 *
 * Methods:
 * - `navigate`: Navigates to the booking details page when the "View Details" button is clicked.
 *
 * UI Elements:
 * - `Card`: Displays booking details in a styled card format.
 * - `CardHeader`: Displays the hotel name and booking ID.
 * - `CardContent`: Displays additional booking details such as room type, dates, guests, and status.
 * - `Button`: A button to navigate to the booking details page.
 * - `Grid`: A Material-UI grid layout for organizing booking cards.
 * - `Box`: A container for styling and spacing.
 *
 * Example Usage:
 * ```tsx
 * const bookings = [
 *   {
 *     bookingId: 1,
 *     userEmail: "user@example.com",
 *     roomId: 101,
 *     roomType: "Double",
 *     hotelId: 1,
 *     hotelName: "Hotel Example",
 *     checkIn: "2025-04-25T14:00",
 *     checkOut: "2025-04-27T11:00",
 *     guests: 2,
 *     status: "CONFIRMED",
 *   },
 * ];
 *
 * <BookingList bookings={bookings} isLoggedIn={true} />
 * ```
 */

import { useNavigate } from "react-router";
import {
  Box,
  Button,
  Card,
  CardContent,
  CardHeader,
  Grid,
  Typography,
} from "@mui/material";

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
