package com.Rev.RevStay.models;

import jakarta.persistence.*;

@Entity
@Table(name = "favorites")

public class Favorite {
    /* I have questions with this part
    @EmbeddedId
    private FavoriteId id;
     */

    //Maps the relations with user and hotel Ids
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("hotelId")
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    // Constructor
    public Favorite() {}

//    public Favorite(User user, Hotel hotel) {
//        this.user = user;
//        this.hotel = hotel;
//        this.id = new FavoriteId(user.getUserId(), hotel.getHotelId());
//    }

//    public FavoriteId getId() {
//        return id;
//    }
//
//    public void setId(FavoriteId id) {
//        this.id = id;
//    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
}
