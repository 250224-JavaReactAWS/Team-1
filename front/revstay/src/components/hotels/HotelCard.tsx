import {
    Card,
    CardContent,
    Typography,
    CardHeader,
    IconButton,
    Button,
    Chip,
    Box,
  } from '@mui/material';
  import FavoriteIcon from '@mui/icons-material/Favorite';
  import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
  import { useContext, useState } from 'react';
  import { authContext } from '../../App';
  import { useNavigate } from 'react-router-dom';
  import HotelImageGallery from './HoltelImageGalery';

  
  interface Props {
    hotel: {
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

    isFavorite: boolean;
    toggleFavorite: (hotelId: number) => void;
  }
  
  const HotelCard: React.FC<Props> = ({ hotel, isFavorite, toggleFavorite }) => {
    const navigate = useNavigate();
    const roleReference = useContext(authContext);
    const isLoggedIn = roleReference?.role !== 'UNAUTHENTICATED';
  
    const [showAllAmenities, setShowAllAmenities] = useState(false);
  
    const handleRoomNavigation = (hotelId: number) => {
      navigate(`/rooms/hotel/${hotelId}`);
    };
  
    const handleUpdateHotel = (hotelId: number) => {
        navigate(`/hotels/${hotelId}`);
    };

    const handleDeleteHotel = (hotelId : number) => {
        navigate(`/hotels/${hotelId}`);
    };

    const amenitiesArray = hotel.amenities
      .replace(/[{}]/g, '')
      .split(',')
      .map((a) => a.trim());
  
    return (
      <Card elevation={3}>
        <HotelImageGallery images={hotel.images} />
        <CardHeader
          title={hotel.name}
          subheader={hotel.location}
          sx={{ mt: 0, mb: 0, pb: 0 }}
          action={
            isLoggedIn && roleReference?.role === 'USER' && (
              <IconButton onClick={() => toggleFavorite(hotel.hotelId)}>
                {isFavorite ? (
                  <FavoriteIcon color="error" />
                ) : (
                  <FavoriteBorderIcon />
                )}
              </IconButton>
            )
          }
        />
        <CardContent>
          <Typography variant="body2" color="text.secondary" gutterBottom>
            {hotel.description}
          </Typography>
          <Typography variant="subtitle2" color="primary">
            Price: {hotel.priceRange}
          </Typography>
  
          <Box mt={2} mb={1}>
            <Typography variant="caption">Amenities:</Typography>
            <Box
              display="flex"
              justifyContent="center"
              flexWrap="wrap"
              gap={1}
              mt={0.5}
              sx={{
                maxHeight: showAllAmenities ? 120 : 25,
                overflowY: 'hidden',
                transition: 'max-height 0.3s ease',
              }}
            >
              {amenitiesArray.map((amenity, index) => (
                <Chip
                  key={index}
                  label={amenity}
                  size="small"
                  color="secondary"
                />
              ))}
            </Box>
          </Box>
  
          <Box mt={2} display="flex"  justifyContent="center" gap={1} flexWrap="wrap">
            <Button
              variant="contained"
              color="primary"
              onClick={() => handleRoomNavigation(hotel.hotelId)}
            >
              See Rooms
            </Button>

            {amenitiesArray.length > 5 && (
              <Box textAlign="center" justifyContent={'center'}>
                <Button
                  size="small"
                  variant="text"
                  onClick={() => setShowAllAmenities((prev) => !prev)}
                >
                  {showAllAmenities ? 'Ver menos' : 'Ver más'}
                </Button>
              </Box>
            )}

            {roleReference?.role === 'OWNER' && (
              <>
                <Button
                    variant="contained"
                    color="success"
                    onClick={() => handleUpdateHotel(hotel.hotelId)}
                >
                    Update
                </Button>

                <Button
                    variant="contained"
                    color="secondary"
                    onClick={() => handleDeleteHotel(hotel.hotelId)}
                >
                    Delete
                    </Button>
              </>
            )}
          </Box>
        </CardContent>
      </Card>
    );
  };
  
  export default HotelCard;
  