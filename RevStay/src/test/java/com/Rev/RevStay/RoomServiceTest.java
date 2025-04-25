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
import java.util.List;
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

    private User owner;
    private Hotel hotel;
    private Room room;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setUserId(1);

        hotel = new Hotel();
        hotel.setHotelId(100);
        hotel.setOwner(owner);

        room = new Room();
        room.setRoomId(200);
        room.setHotel(hotel);
        room.setPrice(BigDecimal.valueOf(150));
        room.setMaxGuests(2);
        room.setRoomType("Double");
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterRoomSuccess() {

        when(hotelDAO.findById(hotel.getHotelId())).thenReturn(Optional.of(hotel));
        when(userDAO.findById(owner.getUserId())).thenReturn(Optional.of(owner));
        when(roomDAO.save(any(Room.class))).thenReturn(room);

        Optional<RoomDTO> result = roomService.register(room, owner.getUserId());

        assertTrue(result.isPresent());
        assertEquals(room.getRoomId(), result.get().getRoomId());
        verify(roomDAO).save(any(Room.class));
    }

    @Test
    void testRegisterRoomHotelNotFound() {
        room.setHotel(hotel);
        when(hotelDAO.findById(hotel.getHotelId())).thenReturn(Optional.empty());

        GenericException ex = assertThrows(GenericException.class, () -> roomService.register(room, owner.getUserId()));
        assertEquals("The hotel does not exist", ex.getMessage());
    }

    @Test
    void testDeleteRoomSuccess() {

        when(roomDAO.findById(room.getRoomId())).thenReturn(Optional.of(room));
        when(userDAO.findById(owner.getUserId())).thenReturn(Optional.of(owner));

        assertDoesNotThrow(() -> roomService.deleteRoom(room.getRoomId(), owner.getUserId()));
        verify(roomDAO, times(1)).delete(room);
    }

    @Test
    void testDeleteRoomUnauthorized() {
        User anotherUser = new User();
        anotherUser.setUserId(999);

        when(roomDAO.findById(room.getRoomId())).thenReturn(Optional.of(room));
        when(userDAO.findById(anotherUser.getUserId())).thenReturn(Optional.of(anotherUser));

        GenericException ex = assertThrows(GenericException.class,
                () -> roomService.deleteRoom(room.getRoomId(), anotherUser.getUserId()));
        assertEquals("You are not authorized to delete this room.", ex.getMessage());
    }

    @Test
    void testUpdateRoomSuccess() {
        Room updatedRoom = new Room();
        updatedRoom.setPrice(BigDecimal.valueOf(200));
        updatedRoom.setMaxGuests(3);
        updatedRoom.setDescription("Updated room");
        updatedRoom.setRoomType("Suite");

        when(roomDAO.findById(room.getRoomId())).thenReturn(Optional.of(room));
        when(userDAO.findById(owner.getUserId())).thenReturn(Optional.of(owner));
        when(roomDAO.save(any(Room.class))).thenReturn(room);

        Optional<RoomDTO> result = roomService.updateRoom(room.getRoomId(), updatedRoom, owner.getUserId());

        assertTrue(result.isPresent());
        assertEquals("Suite", result.get().getRoomType());
    }

    @Test
    void testGetRoomsByHotelId() {
        when(roomDAO.getRoomsByHotelId(hotel.getHotelId())).thenReturn(List.of(room));

        List<RoomDTO> result = roomService.getRoomsByHotelId(hotel.getHotelId());

        assertEquals(1, result.size());
        assertEquals(room.getRoomId(), result.get(0).getRoomId());
    }

}
