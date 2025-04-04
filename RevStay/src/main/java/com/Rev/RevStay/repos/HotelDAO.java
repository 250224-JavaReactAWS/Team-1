package com.Rev.RevStay.repos;

import com.Rev.RevStay.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelDAO extends JpaRepository<Hotel, Integer> {
}
