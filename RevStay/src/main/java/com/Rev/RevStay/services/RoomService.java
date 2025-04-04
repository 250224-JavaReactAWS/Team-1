package com.Rev.RevStay.services;

import com.Rev.RevStay.exceptions.GenericException;
import com.Rev.RevStay.models.Room;

import com.Rev.RevStay.models.User;
import com.Rev.RevStay.repos.HotelDAO;
import com.Rev.RevStay.repos.RoomDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoomService {
    private final RoomDAO roomDAO;
    private final HotelDAO hotelDAO;

    @Autowired
    public RoomService(RoomDAO roomDAO, HotelDAO hotelDAO) { this.roomDAO = roomDAO;
        this.hotelDAO = hotelDAO;
    }

    //TODO Register New ROOM
    public Optional<Room> register(Room roomToBeCreate, User owner){

        if (roomToBeCreate.getHotel() == null || roomToBeCreate.getHotel().getHotelId() == 0) {
            throw new GenericException("Hotel information is required");
        }

        //Validate that the hotel Exits
        int hotelId = roomToBeCreate.getHotel().getHotelId();
        if (!hotelDAO.existsById(hotelId)) {
            throw new GenericException("The hotel does not exist");
        }

        if (roomToBeCreate.getMaxGuests() <= 0){
            throw new GenericException("The number of guests must be at least one");
        }

        if (roomToBeCreate.getHotel().getOwner().equals(owner)) {
            return Optional.of(roomDAO.save(roomToBeCreate));
        } else {
            throw new GenericException("You are not authorized to register rooms in this Hotel.");
        }

    }

    //TODO Delete ROOM
    public void deleteRoom(int roomId, User owner){
        Room roomToBeDelete = roomDAO.findById(roomId)
                .orElseThrow(() -> new GenericException("Room not found"));

        if (roomToBeDelete.getHotel().getOwner().equals(owner)) {
            roomDAO.delete(roomToBeDelete);
        } else {
            throw new GenericException("You are not authorized to modify this room.");
        }
    }

    // TODO Update ROOM
    public Optional<Room> updateRoom(int roomId, Room updatedRoom, User owner) {
        Room existingRoom = roomDAO.findById(roomId)
                .orElseThrow(() -> new GenericException("Room not found"));

        if (existingRoom.getHotel().getOwner().equals(owner)) {
            // The hotel is not the same ??
            if (updatedRoom.getHotel() != null) {
                int hotelId = updatedRoom.getHotel().getHotelId();
                if (!hotelDAO.existsById(hotelId)) {
                    throw new GenericException("The hotel doesn't exist");
                }
                existingRoom.setHotel(updatedRoom.getHotel());
            }

            if (updatedRoom.getPrice() != null && updatedRoom.getPrice().compareTo(BigDecimal.ZERO) > 0) {
                existingRoom.setPrice(updatedRoom.getPrice());
            }

            if (updatedRoom.getMaxGuests() > 0) {
                existingRoom.setMaxGuests(updatedRoom.getMaxGuests());
            }

            if (updatedRoom.getDescription() != null) {
                existingRoom.setDescription(updatedRoom.getDescription());
            }

            if (updatedRoom.getRoomType() != null) {
                existingRoom.setRoomType(updatedRoom.getRoomType());
            }

            return Optional.of(roomDAO.save(existingRoom));
        } else {
            throw new GenericException("You are not authorized to modify this room.");
        }
    }

    //TODO Get Rooms By Hotel Id
    public List<Room> getRoomsByHotelId(int hotelId){
        return roomDAO.getRoomsByHotelId(hotelId);
    }

}
