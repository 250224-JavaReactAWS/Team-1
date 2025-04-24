package com.Rev.RevStay.services;

import com.Rev.RevStay.DTOS.RoomDTO;
import com.Rev.RevStay.exceptions.GenericException;
import com.Rev.RevStay.models.Hotel;
import com.Rev.RevStay.models.Room;

import com.Rev.RevStay.models.User;
import com.Rev.RevStay.repos.HotelDAO;
import com.Rev.RevStay.repos.RoomDAO;
import com.Rev.RevStay.repos.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoomService {
    private final RoomDAO roomDAO;
    private final HotelDAO hotelDAO;
    private final UserDAO userDAO;


    @Autowired
    public RoomService(RoomDAO roomDAO, HotelDAO hotelDAO, UserDAO userDAO) { this.roomDAO = roomDAO;
        this.hotelDAO = hotelDAO;
        this.userDAO = userDAO;
    }

    //Register New ROOM
    public Optional<RoomDTO> register(Room roomToBeCreate, int ownerId){

        if (roomToBeCreate.getHotel() == null || roomToBeCreate.getHotel().getHotelId() == 0) {
            throw new GenericException("Hotel information is required");
        }

        int hotelId = roomToBeCreate.getHotel().getHotelId();
        Hotel hotel = hotelDAO.findById(hotelId)
                .orElseThrow(() -> new GenericException("The hotel does not exist"));

        Optional<User> owner = userDAO.findById(ownerId);
        if (owner.isEmpty()) {
            throw new GenericException("User not found");
        }

        if (!hotel.getOwner().equals(owner.get())) {
            throw new GenericException("You are not authorized to register rooms in this Hotel.");
        }

        roomToBeCreate.setHotel(hotel);

        return Optional.of(convertToDTO(roomDAO.save(roomToBeCreate)));

    }

    //Delete ROOM
    public void deleteRoom(int roomId, int ownerId){
        Room roomToBeDelete = roomDAO.findById(roomId)
                .orElseThrow(() -> new GenericException("Room not found"));
        Optional<User> owner = userDAO.findById(ownerId);
        if (owner.isPresent()){
            if (roomToBeDelete.getHotel().getOwner() == owner.get()) {
                roomDAO.delete(roomToBeDelete);
            } else {
                throw new GenericException("You are not authorized to delete this room.");
            }
        }
    }
    public Optional<RoomDTO> updateRoom(int roomId, Room updatedRoom, int ownerId) {
        Room existingRoom = roomDAO.findById(roomId)
                .orElseThrow(() -> new GenericException("Room not found"));

        User owner = userDAO.findById(ownerId)
                .orElseThrow(() -> new GenericException("User not found"));

        Hotel hotel = existingRoom.getHotel();
        if (hotel.getOwner() == null || !hotel.getOwner().equals(owner)) {
            throw new GenericException("You are not authorized to modify this room.");
        }

        if (updatedRoom.getPrice() != null && updatedRoom.getPrice().compareTo(BigDecimal.ZERO) > 0) {
            existingRoom.setPrice(updatedRoom.getPrice());
        }

        if (updatedRoom.getMaxGuests() > 0) {
            existingRoom.setMaxGuests(updatedRoom.getMaxGuests());
        }

        if (updatedRoom.getDescription() != null && !updatedRoom.getDescription().isBlank()) {
            existingRoom.setDescription(updatedRoom.getDescription());
        }

        if (updatedRoom.getRoomType() != null && !updatedRoom.getRoomType().isBlank()) {
            existingRoom.setRoomType(updatedRoom.getRoomType());
        }

        return Optional.of(convertToDTO(roomDAO.save(existingRoom)));
    }


    //Get Rooms By Hotel Id
    public List<RoomDTO> getRoomsByHotelId(int hotelId){
        return roomDAO.getRoomsByHotelId(hotelId).stream()
                .map(this::convertToDTO).toList();
    }

    private RoomDTO convertToDTO(Room room) { return new RoomDTO(room); }

    public RoomDTO getRoomById(int roomId) {
        Room room = roomDAO.findById(roomId)
                .orElseThrow(() -> new GenericException("Room not found"));
        return convertToDTO(room);
    }
}
