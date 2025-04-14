package com.Rev.RevStay;

import com.Rev.RevStay.DTOS.RoomDTO;
import com.Rev.RevStay.exceptions.GenericException;
import com.Rev.RevStay.models.Hotel;
import com.Rev.RevStay.models.Room;
import com.Rev.RevStay.models.User;
import com.Rev.RevStay.repos.HotelDAO;
import com.Rev.RevStay.repos.RoomDAO;
import com.Rev.RevStay.repos.UserDAO;
import com.Rev.RevStay.services.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class RoomServiceTest {

    @Mock
    private RoomDAO roomDAO;

    @Mock
    private HotelDAO hotelDAO;

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterRoomSuccess() {
        Room room = new Room();
        Hotel hotel = new Hotel();
        hotel.setHotelId(1);
        User owner = new User();
        owner.setUserId(1);
        hotel.setOwner(owner);
        room.setHotel(hotel);
        room.setMaxGuests(2);

        when(hotelDAO.existsById(1)).thenReturn(true);
        when(userDAO.findById(1)).thenReturn(Optional.of(owner));
        when(roomDAO.save(room)).thenReturn(room);

        Optional<RoomDTO> result = roomService.register(room, 1);

        assertTrue(result.isPresent());
        assertEquals(room, result.get());
        verify(roomDAO, times(1)).save(room);
    }

//    @Test
//    void testRegisterRoomHotelNotFound() {
//        Room room = new Room();
//        Hotel hotel = new Hotel();
//        hotel.setHotelId(1);
//        room.setHotel(hotel);
//
//        when(hotelDAO.existsById(1)).thenReturn(false);
//
//        GenericException exception = assertThrows(GenericException.class, () -> roomService.register(room, 1));
//        assertEquals("The hotel does not exist", exception.getMessage());
//    }
//
//    @Test
//    void testDeleteRoomSuccess() {
//        Room room = new Room();
//        Hotel hotel = new Hotel();
//        User owner = new User();
//        owner.setUserId(1);
//        hotel.setOwner(owner);
//        room.setHotel(hotel);
//
//        when(roomDAO.findById(1)).thenReturn(Optional.of(room));
//        when(userDAO.findById(1)).thenReturn(Optional.of(owner));
//
//        assertDoesNotThrow(() -> roomService.deleteRoom(1, 1));
//        verify(roomDAO, times(1)).delete(room);
//    }
//
//    @Test
//    void testDeleteRoomUnauthorized() {
//        Room room = new Room();
//        Hotel hotel = new Hotel();
//        User owner = new User();
//        owner.setUserId(2);
//        hotel.setOwner(owner);
//        room.setHotel(hotel);
//
//        when(roomDAO.findById(1)).thenReturn(Optional.of(room));
//        when(userDAO.findById(1)).thenReturn(Optional.of(new User()));
//
//        GenericException exception = assertThrows(GenericException.class, () -> roomService.deleteRoom(1, 1));
//        assertEquals("You are not authorized to delete this room.", exception.getMessage());
//    }
//
//    @Test
//    void testUpdateRoomSuccess() {
//        Room existingRoom = new Room();
//        existingRoom.setPrice(BigDecimal.valueOf(100));
//        existingRoom.setMaxGuests(2);
//        existingRoom.setDescription("Old description");
//
//        Room updatedRoom = new Room();
//        updatedRoom.setPrice(BigDecimal.valueOf(150));
//        updatedRoom.setMaxGuests(3);
//        updatedRoom.setDescription("New description");
//
//        Hotel hotel = new Hotel();
//        User owner = new User();
//        owner.setUserId(1);
//        hotel.setOwner(owner);
//        existingRoom.setHotel(hotel);
//
//        when(roomDAO.findById(1)).thenReturn(Optional.of(existingRoom));
//        when(userDAO.findById(1)).thenReturn(Optional.of(owner));
//        when(roomDAO.save(existingRoom)).thenReturn(existingRoom);
//
//        Optional<RoomDTO> result = roomService.updateRoom(1, updatedRoom, 1);
//
//        assertTrue(result.isPresent());
//        assertEquals(BigDecimal.valueOf(150), result.get().getPrice());
//        assertEquals(3, result.get().getMaxGuests());
//        assertEquals("New description", result.get().getDescription());
//    }
//
//    @Test
//    void testGetRoomsByHotelId() {
//        roomService.getRoomsByHotelId(1);
//        verify(roomDAO, times(1)).getRoomsByHotelId(1);
//    }
}
