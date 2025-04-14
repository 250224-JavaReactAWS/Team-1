package com.Rev.RevStay;

import com.Rev.RevStay.DTOS.PaymentDTO;
import com.Rev.RevStay.exceptions.GenericException;
import com.Rev.RevStay.models.*;
import com.Rev.RevStay.repos.BookingDAO;
import com.Rev.RevStay.repos.PaymentDAO;
import com.Rev.RevStay.services.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class PaymentServiceTest {

    @Mock
    private PaymentDAO paymentDAO;

    @Mock
    private BookingDAO bookingDAO;

    @InjectMocks
    private PaymentService paymentService;

    private User user;
    private Hotel hotel;
    private Room room;
    private Booking booking;
    private Payment payment;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1);

        hotel = new Hotel();
        hotel.setHotelId(10);
        hotel.setOwner(user);

        room = new Room();
        room.setRoomId(100);
        room.setPrice(BigDecimal.valueOf(200));

        booking = new Booking();
        booking.setBookId(20);
        booking.setUser(user);
        booking.setHotel(hotel);
        booking.setRoom(room);
        booking.setCheckIn(LocalDateTime.now());
        booking.setCheckOut(LocalDateTime.now().plusDays(2));

        payment = new Payment();
        payment.setPaymentId(1);
        payment.setBooking(booking);
        payment.setUser(user);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPaymentsByUserId() {

        when(paymentDAO.getPaymentsByUserId(1)).thenReturn(List.of(payment));

        List<PaymentDTO> result = paymentService.getPaymentsByUserId(1);

        assertEquals(1, result.size());
        assertEquals(payment.getPaymentId(), result.get(0).getPaymentId());
    }

    @Test
    void testGetPaymentsByHotelId() {
        when(paymentDAO.getPaymentsByHotelId(10)).thenReturn(List.of(payment));

        List<PaymentDTO> result = paymentService.getPaymentsByHotelId(10);

        assertEquals(1, result.size());
    }


    @Test
    void testRegisterPayment_Success() {
        when(bookingDAO.findById(20)).thenReturn(Optional.of(booking));
        when(paymentDAO.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<PaymentDTO> result = paymentService.registerPayment(payment, 1, 20);

        assertTrue(result.isPresent());
        assertEquals(PaymentStatus.PENDING, result.get().getPaymentStatus());
        assertEquals(BigDecimal.valueOf(400), result.get().getAmount()); // 2 noches x 200
    }

    @Test
    void testRegisterPayment_BookingNotFound() {
        when(bookingDAO.findById(20)).thenReturn(Optional.empty());

        assertThrows(GenericException.class, () -> paymentService.registerPayment(payment, 1, 20));
    }

    @Test
    void testUpdatePaymentStatus_Success() {
        payment.setPaymentStatus(PaymentStatus.PENDING);
        when(paymentDAO.findById(1)).thenReturn(Optional.of(payment));
        when(bookingDAO.findById(20)).thenReturn(Optional.of(booking));
        when(paymentDAO.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<PaymentDTO> result = paymentService.updatePaymentStatus(1, PaymentStatus.COMPLETED, 1, "OWNER", 20);

        assertTrue(result.isPresent());
        assertEquals(PaymentStatus.COMPLETED, result.get().getPaymentStatus());
    }

    @Test
    void testUpdatePaymentStatus_PaymentNotFound() {
        when(paymentDAO.findById(1)).thenReturn(Optional.empty());

        assertThrows(GenericException.class,
                () -> paymentService.updatePaymentStatus(1, PaymentStatus.COMPLETED, 1, "OWNER", 20));
    }

}
