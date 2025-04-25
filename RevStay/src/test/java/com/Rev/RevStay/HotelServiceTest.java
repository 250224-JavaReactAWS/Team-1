package com.Rev.RevStay;

import com.Rev.RevStay.DTOS.HotelDTO;
import com.Rev.RevStay.exceptions.GenericException;
import com.Rev.RevStay.models.Hotel;
import com.Rev.RevStay.models.User;
import com.Rev.RevStay.repos.HotelDAO;
import com.Rev.RevStay.repos.UserDAO;
import com.Rev.RevStay.services.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HotelServiceTest {

    @Mock
    private HotelDAO hotelDAO;

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private HotelService hotelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllHotels() {
        List<Hotel> hotels = new ArrayList<>();
        Hotel hotel = new Hotel();
        hotel.setOwner(new User());
        hotels.add(hotel);
        when(hotelDAO.findAll()).thenReturn(hotels);

        List<HotelDTO> result = hotelService.getAllHotels();

        assertEquals(1, result.size());
        verify(hotelDAO, times(1)).findAll();
    }

    @Test
    void testGetById() {
        Hotel hotel = new Hotel();
        hotel.setHotelId(1);
        hotel.setOwner(new User());
        when(hotelDAO.findById(1)).thenReturn(Optional.of(hotel));

        Optional<HotelDTO> result = hotelService.getById(1);

        assertTrue(result.isPresent());
        assertEquals(hotel.getHotelId(), result.get().getHotelId());
        verify(hotelDAO, times(1)).findById(1);
    }

    @Test
    void testUpdateHotel_Success() {
        Hotel existingHotel = new Hotel();
        existingHotel.setName("test");
        User owner = new User();
        existingHotel.setOwner(owner);

        Hotel updatedHotel = new Hotel();
        updatedHotel.setName("result");

        when(hotelDAO.findById(1)).thenReturn(Optional.of(existingHotel));
        when(userDAO.findById(1)).thenReturn(Optional.of(owner));
        when(hotelDAO.save(updatedHotel)).thenReturn(updatedHotel);

        HotelDTO result = hotelService.updateHotel(1, 1, updatedHotel);

        assertEquals(updatedHotel.getName(), result.getName());
        verify(hotelDAO, times(1)).save(updatedHotel);
    }

    @Test
    void testUpdateHotel_Failure_OwnerMismatch() {
        Hotel existingHotel = new Hotel();
        User owner = new User();
        User differentOwner = new User();
        existingHotel.setOwner(owner);

        Hotel updatedHotel = new Hotel();

        when(hotelDAO.findById(1)).thenReturn(Optional.of(existingHotel));
        when(userDAO.findById(2)).thenReturn(Optional.of(differentOwner));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            hotelService.updateHotel(1, 2, updatedHotel);
        });

        assertEquals("Owner ID does not match the hotel's owner ID.", exception.getMessage());
    }

    @Test
    void testDeleteHotel_Success() {
        Hotel existingHotel = new Hotel();
        User owner = new User();
        existingHotel.setOwner(owner);

        when(hotelDAO.findById(1)).thenReturn(Optional.of(existingHotel));
        when(userDAO.findById(1)).thenReturn(Optional.of(owner));

        hotelService.deleteHotel(1, 1);

        verify(hotelDAO, times(1)).deleteById(1);
    }

    @Test
    void testDeleteHotel_Failure_HotelNotFound() {
        when(hotelDAO.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            hotelService.deleteHotel(1, 1);
        });

        assertEquals("Hotel with ID 1 does not exist.", exception.getMessage());
    }

    @Test
    void testCreateHotel_Success() {
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        User owner = new User();

        when(hotelDAO.findHotelByName("Test Hotel")).thenReturn(Optional.empty());
        when(userDAO.findById(1)).thenReturn(Optional.of(owner));
        when(hotelDAO.save(hotel)).thenReturn(hotel);

        Optional<HotelDTO> result = hotelService.createHotel(hotel, 1);

        assertTrue(result.isPresent());
        assertEquals(hotel.getName(), result.get().getName());
        verify(hotelDAO, times(1)).save(hotel);
    }

    @Test
    void testCreateHotel_Failure_HotelExists() {
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");

        when(hotelDAO.findHotelByName("Test Hotel")).thenReturn(Optional.of(hotel));

        Exception exception = assertThrows(GenericException.class, () -> {
            hotelService.createHotel(hotel, 1);
        });

        assertEquals("Hotel with name: Test Hotel already exists!", exception.getMessage());
    }
}
