package com.Rev.RevStay;

import com.Rev.RevStay.DTOS.BookingDTO;
import com.Rev.RevStay.exceptions.GenericException;
import com.Rev.RevStay.models.Booking;
import com.Rev.RevStay.models.BookingStatus;
import com.Rev.RevStay.models.Hotel;
import com.Rev.RevStay.models.Room;
import com.Rev.RevStay.models.User;
import com.Rev.RevStay.repos.BookingDAO;
import com.Rev.RevStay.repos.HotelDAO;
import com.Rev.RevStay.repos.RoomDAO;
import com.Rev.RevStay.repos.UserDAO;
import com.Rev.RevStay.services.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookingServiceTest {

    @Mock
    private BookingDAO bookingDAO;

    @Mock
    private RoomDAO roomDAO;

    @Mock
    private HotelDAO hotelDAO;

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private BookingService bookingService;

    private Booking booking;
    private Hotel hotel;
    private Room room;
    private User user;
    private BookingDTO bookingDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        hotel = new Hotel();
        hotel.setHotelId(1);

        room = new Room();
        room.setRoomId(1);

        user = new User();
        user.setUserId(1);
        user.setEmail("test@example.com");

        booking = new Booking();
        booking.setHotel(hotel);
        booking.setRoom(room);
        booking.setCheckIn(LocalDateTime.now().plusDays(1));
        booking.setCheckOut(LocalDateTime.now().plusDays(2));
        booking.setGuests(2);
        booking.setUser(user);
        booking.setStatus(BookingStatus.PENDING);

        bookingDTO = new BookingDTO(
                booking.getBookId(),
                booking.getCheckIn(),
                booking.getCheckOut(),
                booking.getGuests(),
                booking.getStatus().toString(),
                booking.getHotel().getHotelId(),
                booking.getHotel().getName(),
                booking.getRoom().getRoomId(),
                booking.getRoom().getRoomType(),
                booking.getUser().getEmail());
    }

    @Test
    public void testMakeReservation_Success() {
        when(hotelDAO.findById(anyInt())).thenReturn(Optional.of(hotel));
        when(roomDAO.findById(anyInt())).thenReturn(Optional.of(room));
        when(userDAO.findById(anyInt())).thenReturn(Optional.of(user));
        when(bookingDAO.isRoomAvailable(anyInt(), any(), any(), anyInt())).thenReturn(true);
        when(bookingDAO.save(any(Booking.class))).thenReturn(booking);

        Optional<BookingDTO> result = bookingService.makeReservation(booking, 1);

        assertTrue(result.isPresent());
        assertEquals(bookingDTO, result.get());
        verify(bookingDAO, times(1)).save(booking);
    }

    @Test
    public void testMakeReservation_InvalidDetails() {
        booking.setGuests(0);

        assertThrows(IllegalArgumentException.class, () -> bookingService.makeReservation(booking, 1));
        verify(bookingDAO, never()).save(any(Booking.class));
    }

    @Test
    public void testMakeReservation_HotelNotFound() {
        when(hotelDAO.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(GenericException.class, () -> bookingService.makeReservation(booking, 1));
        verify(bookingDAO, never()).save(any(Booking.class));
    }

    // @Test
    // public void testMakeReservation_UserNotFound() {
    // when(hotelDAO.findById(anyInt())).thenReturn(Optional.of(hotel));
    // when(roomDAO.findById(anyInt())).thenReturn(Optional.of(room));
    // when(userDAO.findById(anyInt())).thenReturn(Optional.empty());

    // assertThrows(GenericException.class, () ->
    // bookingService.makeReservation(booking, 1));
    // verify(bookingDAO, never()).save(any(Booking.class));
    // }

    @Test
    public void testMakeReservation_RoomNotAvailable() {
        when(hotelDAO.findById(anyInt())).thenReturn(Optional.of(hotel));
        when(roomDAO.findById(anyInt())).thenReturn(Optional.of(room));
        when(userDAO.findById(anyInt())).thenReturn(Optional.of(user));
        when(bookingDAO.isRoomAvailable(anyInt(), any(), any(), anyInt())).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> bookingService.makeReservation(booking, 1));
        verify(bookingDAO, never()).save(any(Booking.class));
    }

    @Test
    public void testUpdateBookingStatus_CancelledByUser() {
        int bookingId = 1;
        int userId = 1;

        booking.setBookId(bookingId);
        booking.setStatus(BookingStatus.CONFIRMED);

        user.setUserId(userId);
        booking.setUser(user);

        when(bookingDAO.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingDAO.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookingDTO updated = bookingService.updateBookingStatus(bookingId, BookingStatus.CANCELLED, userId);

        assertNotNull(updated);
        assertEquals(BookingStatus.CANCELLED.toString(), updated.getStatus());
        verify(bookingDAO, times(1)).save(booking);
    }

    @Test
    public void testUpdateBookingStatus_ConfirmedByOwner() {
        int bookingId = 1;
        int ownerId = 2;

        booking.setBookId(bookingId);
        booking.setStatus(BookingStatus.PENDING);

        User owner = new User();
        owner.setUserId(ownerId);
        hotel.setOwner(owner);

        when(bookingDAO.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingDAO.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookingDTO updated = bookingService.updateBookingStatus(bookingId, BookingStatus.ACCEPTED, ownerId);

        assertNotNull(updated);
        assertEquals(BookingStatus.ACCEPTED.toString(), updated.getStatus());
        verify(bookingDAO, times(1)).save(booking);
    }

    @Test
    public void testUpdateBookingStatus_InvalidStatus() {
        when(bookingDAO.findById(anyInt())).thenReturn(Optional.of(booking));

        assertThrows(IllegalArgumentException.class, () -> {
            BookingStatus invalidStatus = BookingStatus.valueOf("NOT_A_REAL_STATUS");
            bookingService.updateBookingStatus(1, invalidStatus, 1);
        });

        verify(bookingDAO, never()).save(any(Booking.class));
    }

    @Test
    public void testUpdateBookingStatus_BookingNotFound() {
        when(bookingDAO.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(GenericException.class, () -> bookingService.updateBookingStatus(1, BookingStatus.CANCELLED, 1));
        verify(bookingDAO, never()).save(any(Booking.class));
    }
}
