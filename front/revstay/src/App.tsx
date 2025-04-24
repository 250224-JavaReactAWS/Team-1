import { BrowserRouter, Routes, Route } from 'react-router'
import './App.css'
import Login from './components/login/Login'
import Nav from './components/nav/Nav'
import HotelPage from './components/hotels/HotelPage'
import OwnerHotel from './components/hotels/HotelsOwner'

//import Bookings from './components/bookings/Bookings'
import HotelFavorite from './components/hotels/HotelFavorite'
import RegisterHotel from './components/hotels/RegisterHotel';
import BookingRoom from './components/bookings/BoookingRoom'
//import Enrollments from './components/enrollments/Enrollments'
//import Courses from './components/courses/Courses'
import Register from './components/register/Register'
import { createContext, useEffect, useState } from 'react'
import axios from 'axios'
import { ThemeProvider } from '@emotion/react'
import { theme } from './theme'
import RoomList from './components/rooms/RoomList'
import BookingPage from './components/bookings/BookingPage'
import UpdateHotel from './components/hotels/UpdateHotel'
import DeleteHotel from './components/hotels/DeleteHotel'
import AddRoom from './components/rooms/AddRoom'
import DeleteRoom from './components/rooms/DeleteRoom';
import EditRoom from './components/rooms/EditRoom';

/*
Need to run 
npm install @mui/material @emotion/react @emotion/styled
npm i react-router
npm install axios
*/

export interface AuthContextType{
  role: "USER" | "OWNER" | "UNAUTHENTICATED",
  setRole: (role: "USER" | "OWNER" | "UNAUTHENTICATED") => void
}

export const authContext = createContext<AuthContextType | null>(null)

function App() {

  const savedUser = localStorage.getItem("user");
  const initialRole = savedUser ? JSON.parse(savedUser).role : "UNAUTHENTICATED";
  const [role, setRole] = useState<"USER" | "OWNER" | "UNAUTHENTICATED">(initialRole);


  //const [role, setRole] = useState<"USER" | "OWNER" | "UNAUTHENTICATED">("UNAUTHENTICATED")

  // Right now we're logged in but if I hard refresh all of the state variables disappear, I could just check
  // if the cookie exists and store a little more information inside of local storage (this is totally valid
  // and probably the easiest way to do this) but for consistency with the backend I will create an endpoint
  // that retrieves the current session info

  useEffect(() => {
    axios.get<"USER" | "OWNER">("http://localhost:8080/users/session", { withCredentials: true })
      .then(res => {
        setRole(res.data);
      })
      .catch(err => {
        setRole("UNAUTHENTICATED");
        console.log(err);
      });
  }, []);
  

  // useEffect(() => {
  //   axios.get<"USER" | "OWNER">("http://localhost:8080/users/session", {withCredentials: true})
  //   .then(res => {
  //     setRole(res.data)
  //   })
  //   .catch(err => {
  //     setRole("UNAUTHENTICATED")
  //     console.log(err)
  //   })
  // }, [])
  

  return (
    <>
    <ThemeProvider theme={theme}>
      <authContext.Provider value={{role, setRole}} >
        <BrowserRouter>
          <Nav />

          <Routes>
            <Route path='/' element={<HotelPage />} />
            <Route path='/hotelFav' element={<HotelFavorite />} />
            <Route path="/registerHotel" element={<RegisterHotel />} />
            <Route path="/ownerHotels" element={<OwnerHotel />} />
            <Route path="hotels/update/:hotelId" element={<UpdateHotel hotel={{ id: '', name: '', location: '' }} onUpdate={() => {}} />} />
            <Route path="/hotels/delete/:hotelId" element={<DeleteHotel />} />

            <Route path='bookings' element={<BookingPage />}/>
            <Route path='bookings/reserve' element={<BookingRoom />}/>
            <Route path='login' element={<Login />} />
            <Route path='register' element={<Register />} />
            <Route path='rooms/hotel/:hotelId' element={<RoomList />} />
            
            {/* Add the AddRoom route */}
            <Route path='/rooms/add/:hotelId' element={<AddRoom />} />

            {/* Add the EditRoom and DeleteRoom routes */}
            <Route path='/rooms/edit/:roomId' element={<EditRoom />} />
            <Route path='/rooms/delete/:roomId' element={<DeleteRoom />} />
          </Routes>
        
        </BrowserRouter>
      
      </authContext.Provider>
      </ThemeProvider>
    </>
  )
}

export default App
