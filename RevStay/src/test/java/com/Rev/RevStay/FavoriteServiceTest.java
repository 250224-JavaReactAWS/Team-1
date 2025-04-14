package com.Rev.RevStay;

import com.Rev.RevStay.DTOS.HotelDTO;
import com.Rev.RevStay.exceptions.GenericException;
import com.Rev.RevStay.models.Hotel;
import com.Rev.RevStay.models.User;
import com.Rev.RevStay.repos.HotelDAO;
import com.Rev.RevStay.repos.UserDAO;
import com.Rev.RevStay.services.HotelService;
import com.Rev.RevStay.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    HotelDAO hotelDAO;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private HotelService hotelService;

    private User user;
    private Hotel hotel;

    @BeforeEach
    void setUp(){
        user = new User();
        user.setUserId(1);
        user.setFavoriteHotels(new HashSet<>());

        hotel = new Hotel();
        hotel.setHotelId(1);
        hotel.setOwner(new User());
    }

   @Test
   void testAddHotelToFavorites_Success() {
       Mockito.when(userDAO.findById(1)).thenReturn(Optional.of(user));
       Mockito.when(hotelDAO.findById(1)).thenReturn(Optional.of(hotel));

       userService.addHotelToFavorites(1, 1);

       assertTrue(user.getFavoriteHotels().contains(hotel));
       Mockito.verify(userDAO).save(user);
   }

   @Test
   void testAddHotelToFavorites_HotelNotFound() {
       Mockito.when(hotelDAO.findById(1)).thenReturn(Optional.empty());

       GenericException thrown = assertThrows(GenericException.class,
               () -> userService.addHotelToFavorites(1, 1));

       assertEquals("The hotel does not exist", thrown.getMessage());
   }

   @Test
   void testAddHotelToFavorites_UserNotFound() {
       Mockito.when(hotelDAO.findById(1)).thenReturn(Optional.of(hotel));
       Mockito.when(userDAO.findById(1)).thenReturn(Optional.empty());

       GenericException thrown = assertThrows(GenericException.class,
               () -> userService.addHotelToFavorites(1, 1));

       assertEquals("The user does not exist", thrown.getMessage());
   }

   @Test
   void testRemoveHotelFromFavorites_Success() {
       user.getFavoriteHotels().add(hotel);

       Mockito.when(userDAO.findById(1)).thenReturn(Optional.of(user));
       Mockito.when(hotelDAO.findById(1)).thenReturn(Optional.of(hotel));

       userService.removeHotelFromFavorites(1, 1);

       assertFalse(user.getFavoriteHotels().contains(hotel));
       Mockito.verify(userDAO).save(user);
   }

   @Test
   void testRemoveHotelFromFavorites_HotelNotFound() {
       Mockito.when(hotelDAO.findById(1)).thenReturn(Optional.empty());

       GenericException thrown = assertThrows(GenericException.class,
               () -> userService.removeHotelFromFavorites(1, 1));

       assertEquals("The hotel does not exist", thrown.getMessage());
   }

   @Test
   void testRemoveHotelFromFavorites_UserNotFound() {
       Mockito.when(hotelDAO.findById(1)).thenReturn(Optional.of(hotel));
       Mockito.when(userDAO.findById(1)).thenReturn(Optional.empty());

       GenericException thrown = assertThrows(GenericException.class,
               () -> userService.removeHotelFromFavorites(1, 1));

       assertEquals("The user does not exist", thrown.getMessage());
   }

   @Test
   void testFindFavoriteHotelsByUserId() {
       List<Hotel> expectedHotels = List.of(hotel);
       Mockito.when(hotelDAO.findFavoriteHotelsByUserId(1)).thenReturn(expectedHotels);

       List<HotelDTO> actualHotels = hotelService.findFavoriteHotelsByUserId(1);

       assertEquals(expectedHotels.size(), actualHotels.size());
       Mockito.verify(hotelDAO).findFavoriteHotelsByUserId(1);
   }

}
