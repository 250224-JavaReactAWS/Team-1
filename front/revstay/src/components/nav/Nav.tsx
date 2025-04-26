import { AppBar, Button, Toolbar, Typography } from "@mui/material";
import { useContext } from "react";
import { useNavigate } from "react-router";
import { authContext } from "../../App";
import axios from "axios";

/**
 * `Nav` is a React functional component for rendering the navigation bar.
 * 
 * This component dynamically adjusts the navigation options based on the user's role 
 * (e.g., "USER", "OWNER", or "UNAUTHENTICATED"). It also provides a logout functionality 
 * that clears the user's session and redirects them to the login page.
 * 
 * Context:
 * - `authContext`: Provides the user's role and a function to update the role.
 * 
 * Methods:
 * - `logout`: Logs the user out by clearing their session, updating the role to "UNAUTHENTICATED", 
 *   and redirecting them to the login page.
 * 
 * Navigation Behavior:
 * - "USER":
 *   - Displays options for viewing hotels, bookings, and favorite hotels.
 * - "OWNER":
 *   - Displays options for managing owned hotels and registering new hotels.
 * - "UNAUTHENTICATED":
 *   - Displays options for logging in or registering.
 * 
 * API Endpoint:
 * - POST `http://52.90.96.54:8080/users/logout`: Logs the user out by clearing their session.
 * 
 * UI Elements:
 * - `AppBar`: A Material-UI component for the navigation bar.
 * - `Toolbar`: A Material-UI container for organizing navigation items.
 * - `Typography`: Displays the application title ("RevStay").
 * - `Button`: Provides navigation options and logout functionality.
 * 
 * Example Usage:
 * ```tsx
 * <Nav />
 * ```
 */
function Nav() {
  /*
    I could set this navbar up the way I did before with the Link tag but another thing I can do is the 
    useNavigate hook which allows you to redirect to different pages as needed

    In this class we need should know if the user is logged in so we can control whether or not they see specific
    options
    */
  const navigate = useNavigate();

  // We can use the value for the logged in user to control what values they see
  const roleReference = useContext(authContext);

  let logout = () => {
    roleReference?.setRole("UNAUTHENTICATED");
    navigate("/login");
    // Let's create a logout endpoint
    axios.post(
      "http://52.90.96.54:8080/users/logout",
      {},
      { withCredentials: true }
    );
  };

  return (
    <>
      <AppBar position="fixed">
        <Toolbar>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            RevStay
          </Typography>
          {(!roleReference || roleReference.role != "OWNER") && (
            <Button color="inherit" onClick={() => navigate("/")}>
              Hotels
            </Button>
          )}
          {roleReference?.role === "USER" && (
            <Button color="inherit" onClick={() => navigate("/bookings")}>
              Bookings
            </Button>
          )}
          {roleReference?.role === "USER" && (
            <Button color="inherit" onClick={() => navigate("/hotelFav")}>
              Favorites
            </Button>
          )}
          {roleReference?.role === "OWNER" && (
            <Button color="inherit" onClick={() => navigate("/ownerHotels")}>
              Hotels
            </Button>
          )}
          {roleReference?.role === "OWNER" && (
            <Button color="inherit" onClick={() => navigate("/registerHotel")}>
              Register Hotel
            </Button>
          )}

          {roleReference?.role === "UNAUTHENTICATED" ? (
            <>
              <Button color="inherit" onClick={() => navigate("/login")}>
                Login
              </Button>
              <Button color="inherit" onClick={() => navigate("/register")}>
                Register
              </Button>
            </>
          ) : (
            <Button color="inherit" onClick={logout}>
              Logout
            </Button>
          )}
        </Toolbar>
      </AppBar>
      <Toolbar />
    </>
  );
}
export default Nav;
