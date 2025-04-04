package com.Rev.RevStay.services;

import com.Rev.RevStay.models.Hotel;
import com.Rev.RevStay.repos.HotelDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {
    private final HotelDAO hotelDAO;

    @Autowired
    public HotelService(HotelDAO hotelDAO) { this.hotelDAO = hotelDAO; }

    //TODO Get All hotels
    public List<Hotel> getAllHotels(){ return hotelDAO.findAll();}

    //TODO Get HotelById
    public Optional<Hotel> getById(int hotelId){ return hotelDAO.findById(hotelId);}
    
}
