package com.Rev.RevStay.repos;

import com.Rev.RevStay.models.Hotel;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelDAO extends JpaRepository<Hotel, Integer> {
        
    @Query("SELECT h FROM Hotel h WHERE h.name = :name")
    Optional<Hotel> findHotelByName(@Param("name") String name);

}
