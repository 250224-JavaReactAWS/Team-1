
import React from 'react';
import {
  Card,
  CardContent,
  Typography,
  Grid,
  Box,
  Chip,
  Avatar,
  CardHeader,
} from '@mui/material';

// Define el tipo de hotel
type Hotel = {
  hotelId: number;
  name: string;
  location: string;
  description: string;
  amenities: string;
  priceRange: string;
  images: string[];
  createdAt: string;
  ownerEmail: string;
  ownerFullName: string;
};

interface Props {
  hotels: Hotel[];
}

const HotelList: React.FC<Props> = ({ hotels }) => {
  return (
    <Box p={4}>
      <Grid container spacing={3}>
        {hotels.map((hotel) => (
          <Grid item xs={12} sm={6} md={4} key={hotel.hotelId}>
            <Card elevation={3}>
              <CardHeader
                avatar={<Avatar>{hotel.ownerFullName[0]}</Avatar>}
                title={hotel.name}
                subheader={hotel.location}
              />
              <CardContent>
                <Typography variant="body2" color="text.secondary" gutterBottom>
                  {hotel.description}
                </Typography>
                <Typography variant="subtitle2" color="primary">
                  Precio: {hotel.priceRange}
                </Typography>
                <Box mt={2} mb={1}>
                  <Typography variant="caption">Amenidades:</Typography>
                  <Box display="flex" flexWrap="wrap" gap={1} mt={0.5}>
                    {hotel.amenities
                      .replace(/[{}]/g, '')
                      .split(',')
                      .map((amenity, index) => (
                        <Chip
                          key={index}
                          label={amenity.trim()}
                          size="small"
                          color="secondary"
                        />
                      ))}
                  </Box>
                </Box>
                <Typography variant="caption" color="text.secondary">
                  Propietario: {hotel.ownerFullName} ({hotel.ownerEmail})
                </Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
};

export default HotelList;
