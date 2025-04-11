
export interface IHotel {

    hotelId: number;
    name: string;
    location: string;
    description?: string;
    amenities: string[];
    priceRange?: string;
    images: string[];
    owner: {
      id: number;
      name?: string;
      email?: string;
      userType?: string;
    };

  }
  