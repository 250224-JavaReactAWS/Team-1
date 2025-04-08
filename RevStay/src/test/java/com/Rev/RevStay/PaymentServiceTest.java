package com.Rev.RevStay;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPaymentsByUserId() {
        int userId = 1;
        List<Payment> mockPayments = List.of(new Payment());
        when(paymentDAO.getPaymentsByUserId(userId)).thenReturn(mockPayments);

        List<Payment> payments = paymentService.getPaymentsByUserId(userId);

        assertEquals(mockPayments, payments);
        verify(paymentDAO, times(1)).getPaymentsByUserId(userId);
    }

    @Test
    void testGetPaymentsByHotelId() {
        int hotelId = 1;
        List<Payment> mockPayments = List.of(new Payment());
        when(paymentDAO.getPaymentsByHotelId(hotelId)).thenReturn(mockPayments);

        List<Payment> payments = paymentService.getPaymentsByHotelId(hotelId);

        assertEquals(mockPayments, payments);
        verify(paymentDAO, times(1)).getPaymentsByHotelId(hotelId);
    }

    @Test
    void testRegisterPayment_Success() {
        int userId = 1;
        int bookId = 1;
        Booking mockBooking = new Booking();
        Room mockRoom = new Room();
        mockRoom.setPrice(BigDecimal.valueOf(100)); // Ensure Room class has setPrice method
        mockBooking.setRoom(mockRoom); // Ensure Booking class has setRoom method

        User mockUser = new User();
        mockUser.setUserId(userId); // Ensure User class has setId method
        mockBooking.setUser(mockUser); // Ensure Booking class has setUser method
        mockBooking.setCheckIn(LocalDateTime.now());
        mockBooking.setCheckOut(LocalDateTime.now().plusDays(2));
        Payment mockPayment = new Payment();

        when(bookingDAO.findById(bookId)).thenReturn(Optional.of(mockBooking));
        when(paymentDAO.save(any(Payment.class))).thenReturn(mockPayment);

        Optional<Payment> payment = paymentService.registerPayment(mockPayment, userId, bookId);

        assertTrue(payment.isPresent());
        assertEquals(mockPayment, payment.get());
        verify(bookingDAO, times(1)).findById(bookId);
        verify(paymentDAO, times(1)).save(mockPayment);
    }

    @Test
    void testRegisterPayment_BookingNotFound() {
        int userId = 1;
        int bookId = 1;
        Payment mockPayment = new Payment();

        when(bookingDAO.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(GenericException.class, () -> paymentService.registerPayment(mockPayment, userId, bookId));
        verify(bookingDAO, times(1)).findById(bookId);
        verify(paymentDAO, never()).save(any(Payment.class));
    }

    @Test
    void testUpdatePaymentStatus_Success() {
        int paymentId = 1;
        PaymentStatus newStatus = PaymentStatus.COMPLETED;
        Payment mockPayment = new Payment();
        mockPayment.setPaymentStatus(PaymentStatus.PENDING);

        when(paymentDAO.findById(paymentId)).thenReturn(Optional.of(mockPayment));
        when(paymentDAO.save(any(Payment.class))).thenReturn(mockPayment);

        Optional<Payment> updatedPayment = paymentService.updatePaymentStatus(paymentId, newStatus);

        assertTrue(updatedPayment.isPresent());
        assertEquals(newStatus, updatedPayment.get().getPaymentStatus());
        verify(paymentDAO, times(1)).findById(paymentId);
        verify(paymentDAO, times(1)).save(mockPayment);
    }

    @Test
    void testUpdatePaymentStatus_PaymentNotFound() {
        int paymentId = 1;
        PaymentStatus newStatus = PaymentStatus.COMPLETED;

        when(paymentDAO.findById(paymentId)).thenReturn(Optional.empty());

        assertThrows(GenericException.class, () -> paymentService.updatePaymentStatus(paymentId, newStatus));
        verify(paymentDAO, times(1)).findById(paymentId);
        verify(paymentDAO, never()).save(any(Payment.class));
    }
}
