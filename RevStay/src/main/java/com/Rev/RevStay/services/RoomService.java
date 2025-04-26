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

/**
 * Service class for managing room-related operations such as registering,
 * updating,
 * deleting, and retrieving rooms.
 * 
 * This class provides methods to:
 * - Register a new room.
 * - Update an existing room.
 * - Delete a room.
 * - Retrieve rooms by hotel ID.
 * - Retrieve a room by its ID.
 * 
 * It uses `RoomDAO`, `HotelDAO`, and `UserDAO` for database interactions.
 * 
 * Exceptions:
 * - Throws `GenericException` for invalid inputs, unauthorized actions, or when
 * required entities are not found.
 * 
 * Annotations:
 * - `@Service`: Marks this class as a Spring service component.
 * - `@Transactional`: Ensures that database operations are executed within a
 * transaction.
 */
@Service
@Transactional
public class RoomService {

    private final RoomDAO roomDAO;
    private final HotelDAO hotelDAO;
    private final UserDAO userDAO;

    /**
     * Constructor for RoomService.
     * 
     * @param roomDAO  Data access object for room-related operations.
     * @param hotelDAO Data access object for hotel-related operations.
     * @param userDAO  Data access object for user-related operations.
     */
    @Autowired
    public RoomService(RoomDAO roomDAO, HotelDAO hotelDAO, UserDAO userDAO) {
        this.roomDAO = roomDAO;
        this.hotelDAO = hotelDAO;
        this.userDAO = userDAO;
    }

    /**
     * Registers a new room.
     * 
     * @param roomToBeCreate The room to be registered.
     * @param ownerId        The ID of the owner registering the room.
     * @return An Optional containing the registered RoomDTO.
     * @throws GenericException if the hotel does not exist, the owner is not found,
     *                          or the owner is not authorized to register the room.
     */
    public Optional<RoomDTO> register(Room roomToBeCreate, int ownerId) {

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

    /**
     * Deletes a room.
     * 
     * @param roomId  The ID of the room to be deleted.
     * @param ownerId The ID of the owner attempting to delete the room.
     * @throws GenericException if the room does not exist or the owner is not
     *                          authorized to delete the room.
     */
    public void deleteRoom(int roomId, int ownerId) {
        Room roomToBeDelete = roomDAO.findById(roomId)
                .orElseThrow(() -> new GenericException("Room not found"));
        Optional<User> owner = userDAO.findById(ownerId);
        if (owner.isPresent()) {
            if (roomToBeDelete.getHotel().getOwner() == owner.get()) {
                roomDAO.delete(roomToBeDelete);
            } else {
                throw new GenericException("You are not authorized to delete this room.");
            }
        }
    }

    /**
     * Updates an existing room.
     * 
     * @param roomId      The ID of the room to be updated.
     * @param updatedRoom The updated room details.
     * @param ownerId     The ID of the owner attempting to update the room.
     * @return An Optional containing the updated RoomDTO.
     * @throws GenericException if the room or owner does not exist, or the owner is
     *                          not authorized to update the room.
     */
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

    /**
     * Retrieves all rooms by hotel ID.
     * 
     * @param hotelId The ID of the hotel whose rooms are to be retrieved.
     * @return A list of RoomDTOs for the rooms in the specified hotel.
     */
    public List<RoomDTO> getRoomsByHotelId(int hotelId) {
        return roomDAO.getRoomsByHotelId(hotelId).stream()
                .map(this::convertToDTO).toList();
    }

    /**
     * Retrieves a room by its ID.
     * 
     * @param roomId The ID of the room to be retrieved.
     * @return The RoomDTO for the specified room.
     * @throws GenericException if the room does not exist.
     */
    public RoomDTO getRoomById(int roomId) {
        Room room = roomDAO.findById(roomId)
                .orElseThrow(() -> new GenericException("Room not found"));
        return convertToDTO(room);
    }

    /**
     * Converts a Room entity to a RoomDTO.
     * 
     * @param room The Room entity to be converted.
     * @return The corresponding RoomDTO.
     */
    private RoomDTO convertToDTO(Room room) {
        return new RoomDTO(room);
    }
}
