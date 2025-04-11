import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { IHotel } from '../../interfaces/IHotel';

const AddHotel: React.FC = () => {
  const navigate = useNavigate();
  const [hotel, setHotel] = useState<Partial<IHotel>>({
    name: '',
    location: '',
    description: '',
    priceRange: '',
    amenities: [],
    images: [],
  });

  const [newAmenity, setNewAmenity] = useState('');

  const addAmenity = () => {
    if (newAmenity.trim()) {
      setHotel(prev => ({
        ...prev,
        amenities: [...(prev.amenities || []), newAmenity.trim()]
      }));
      setNewAmenity('');
    }
  };

  const removeAmenity = (indexToRemove: number) => {
    setHotel(prev => ({
      ...prev,
      amenities: prev.amenities?.filter((_, index) => index !== indexToRemove)
    }));
  };
  

  const [isAuthorized, setIsAuthorized] = useState(false);

  useEffect(() => {
    const userData = localStorage.getItem('user');
    if (userData) {
      const user = JSON.parse(userData);
      if (user.userType === 'OWNER') {
        setIsAuthorized(true);
      }
    }
  }, []);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;

    if (name === 'amenities' || name === 'images') {
        setHotel(prev => ({
          ...prev,
          [name]: value ? value.split(',').map((item) => item.trim()) : []
        }));
      } else {
        setHotel(prev => ({
          ...prev,
          [name]: value
        }));
      }
    };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const payload = {
        ...hotel,
        amenities: Array.isArray(hotel.amenities) ? hotel.amenities : [],
        images: Array.isArray(hotel.images) ? hotel.images : []
      };    

    try {
        console.log("Payload enviado al backend:", payload);
        await axios.put('http://localhost:8080/hotels/register', payload, {
        withCredentials: true // importante para enviar la cookie de sesión
      });

      alert('Hotel registrado correctamente');
      navigate('/');
    } catch (error) {
      console.error('Error al registrar el hotel:', error);
      alert('No se pudo registrar el hotel. ¿Estás logueado como OWNER?');
    }
  };

  if (!isAuthorized) {
    return <p>No tienes permiso para acceder a esta sección.</p>;
  }

  return (
    <div>
      <h2>Registrar nuevo hotel</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="name"
          placeholder="Nombre del hotel"
          value={hotel.name || ''}
          onChange={handleChange}
          required
        />
        <input
          type="text"
          name="location"
          placeholder="Ubicación"
          value={hotel.location || ''}
          onChange={handleChange}
          required
        />
        <textarea
          name="description"
          placeholder="Descripción"
          value={hotel.description || ''}
          onChange={handleChange}
        />
        <div>
            <label>Amenidades:</label>
            <div style={{ display: 'flex', gap: '10px', alignItems: 'center', marginBottom: '10px' }}>
            <input
                type="text"
                value={newAmenity}
                onChange={(e) => setNewAmenity(e.target.value)}
                placeholder="Nueva amenidad"
            />
            <button type="button" onClick={addAmenity}>Agregar</button>
            </div>
        
            <ul>
            {hotel.amenities?.map((amenity, index) => (
                <li key={index}>
                {amenity}{" "}
                <button type="button" onClick={() => removeAmenity(index)}>✖</button>
                </li>
            ))}
            </ul>
        </div>

        <input
          type="text"
          name="priceRange"
          placeholder="Rango de precios"
          value={hotel.priceRange || ''}
          onChange={handleChange}
        />
        <input
          type="text"
          name="images"
          placeholder="URLs de imágenes (separadas por comas)"
          onChange={handleChange}
        />
        <button type="submit">Registrar Hotel</button>
      </form>
    </div>
  );
};



export default AddHotel;
