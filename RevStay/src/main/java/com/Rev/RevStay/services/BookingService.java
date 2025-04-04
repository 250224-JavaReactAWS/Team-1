package com.Rev.RevStay.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Rev.RevStay.repos.BookingDAO;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BookingService {
    
    private final BookingDAO bookingDAO;
    
    @Autowired
    public BookingService(BookingDAO bookingDAO){
        this.bookingDAO = bookingDAO;
    }
}
