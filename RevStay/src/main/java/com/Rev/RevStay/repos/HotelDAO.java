package com.Rev.RevStay.repos;

import com.Rev.RevStay.models.Hotel;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelDAO extends JpaRepository<Hotel, Integer> {
        
    Optional<Hotel> findHotelByName(String name);

}
