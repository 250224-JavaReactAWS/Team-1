import axios from "axios";
import { ChangeEvent, SyntheticEvent, useContext, useState } from "react";
import { IUser } from "../../interfaces/IUser";
import {
  Box,
  Button,
  InputAdornment,
  Paper,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import { authContext } from "../../App";
import { useNavigate } from "react-router";
import EmailIcon from "@mui/icons-material/Email";
import LockIcon from "@mui/icons-material/Lock";
import Grid from "@mui/material/Grid";

/**
 * `Login` is a React functional component for handling user login.
 * 
 * This component provides a form for users to input their email and password to log in. 
 * It sends the login credentials to the backend API and handles authentication, 
 * role assignment, and navigation based on the user's role.
 * 
 * State:
 * - `email` (string): Stores the user's email input.
 * - `password` (string): Stores the user's password input.
 * - `error` (boolean): Indicates whether there was an error during login.
 * 
 * Context:
 * - `authContext`: Provides functions to set the user's role after successful login.
 * 
 * Methods:
 * - `updateEmail`: Updates the `email` state when the user types in the email field.
 * - `updatePassword`: Updates the `password` state when the user types in the password field.
 * - `login`: Sends the login credentials to the backend API, handles authentication, 
 *   and sets the user's role and navigation based on the response.
 * - `submitForm`: Prevents the default form submission behavior and triggers the `login` method.
 * 
 * API Endpoint:
 * - POST `http://52.90.96.54:8080/users/login`: Authenticates the user and returns their details.
 * 
 * UI Elements:
 * - `Box`: A Material-UI container for layout and styling.
 * - `Paper`: A Material-UI paper component for styling the login form.
 * - `Grid`: A Material-UI grid layout for organizing the form and logo.
 * - `TextField`: Input fields for email and password.
 * - `Button`: A button to submit the login form.
 * - `Typography`: Displays the form title and error messages.
 * - `InputAdornment`: Adds icons to the input fields for email and password.
 * 
 * Behavior:
 * - If the login is successful, the user's role is set, and they are redirected based on their role:
 *   - "OWNER": Redirects to `/ownerHotels`.
 *   - Other roles: Redirects to the home page (`/`).
 * - If the login fails, an error message is displayed, and the user's role is set to "UNAUTHENTICATED".
 * 
 * Example Usage:
 * ```tsx
 * <Login />
 * ```
 */
function Login() {
  /*
  Inside of this class I want to send an axios request to login my user so let's do that
  */

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(false);

  // Let's use the context to get the role functions
  const roleReference = useContext(authContext);

  // Whenever we log in lets nav to the courses page
  const navigate = useNavigate();

  let updateEmail = (e: ChangeEvent<HTMLInputElement>) => {
    setEmail(e.target.value);
  };

  let updatePassword = (e: ChangeEvent<HTMLInputElement>) => {
    setPassword(e.target.value);
  };

  let login = async () => {
    // This function should make our axios request
    try {
      let res = await axios.post<IUser>(
        "http://52.90.96.54:8080/users/login",
        // Data is the body of our request
        {
          email: email,
          passwordHash: password, // Cambia el nombre de la propiedad
        },
        // The next option is for a config object
        { withCredentials: true } // This allows for the JSESSIONID to be passed
      );

      console.log(res);
      roleReference?.setRole(res.data.userType);
      localStorage.setItem("user", JSON.stringify(res.data));

      if (res.data.userType === "OWNER") {
        navigate("/ownerHotels");
      } else {
        navigate("/");
      }
    } catch (error) {
      setError(true);
      console.log(error);
      roleReference?.setRole("UNAUTHENTICATED");
    }
  };

  let submitForm = async (e: SyntheticEvent<HTMLFormElement>) => {
    e.preventDefault(); // This prevents the form from clearing until we want it to

    login();
  };

  // return (

  //   <Box
  //   sx={{
  //     color: 'black',
  //     border: '2px solid black',
  //     borderRadius: '10px',
  //     width: '100%',
  //     height: '50%',
  //     margin: 'auto',
  //     alignContent: 'center',
  //     textAlign: 'center',
  //     backgroundColor: 'white',
  //   }}
  //   >
  //     <form onSubmit={submitForm} style={{margin: '20px'}}>
  //       <Typography variant='h2'>
  //         Login
  //       </Typography>

  //       {error && <Typography color="error">
  //         Username or Password Incorrect
  //       </Typography>}

  //       <TextField
  //        required
  //         id="email-input"
  //         label="Email"
  //         variant="outlined"
  //         value={email}
  //         onChange={updateEmail}
  //         type="email"
  //       />

  //       <br/>
  //       <br/>
  //       <br/>
  //       <TextField
  //         required
  //         id="password-input"
  //         label="Password"
  //         variant="outlined"
  //         value={password}
  //         onChange={updatePassword}
  //         type="password"
  //       />

  //       <br />
  //       <br />
  //       <br />

  //       {/* Forms behave slightly different than divs, we can use it for additional validation
  //       or taking in input. It's important to note that we should prevent default to prevent the form from
  //       just clearing on submit */}
  //       <Button variant="contained" type="submit">Login!</Button>
  //     </form>
  //   </Box>
  // )

  return (
    <>
      <Box
        sx={{
          position: "fixed",
          top: 0,
          left: 0,
          width: "100%",
          height: "100%",
          background:
            "linear-gradient(135deg, #3f51b5 0%,rgb(255, 81, 0) 100%)",
          zIndex: -1,
        }}
      />
      <Box
        sx={{
          position: "relative",
          width: "100%",
          height: "100%",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        <Paper
          elevation={6}
          sx={{
            width: { xs: "90%", md: "90vh" },
            borderRadius: 4,
            overflow: "hidden",
            zIndex: 1, // Para que el login esté por delante
          }}
        >
          <Grid container sx={{ minHeight: "60vh" }}>
            <Grid
              size={4}
              sx={{
                backgroundColor: "#f5f5f5",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                p: 4,
              }}
            >
              <Box
                component="img"
                src="public\RevStayLogo.jpeg"
                alt="Logo"
                sx={{ width: "100%", maxWidth: 180 }}
              />
            </Grid>

            <Grid size={6} sx={{ p: 2, margin: "0 auto", marginTop: "5%" }}>
              <form onSubmit={submitForm}>
                <Stack spacing={3}>
                  <Typography variant="h4" fontWeight="bold" textAlign="center">
                    Login
                  </Typography>

                  {error && (
                    <Typography color="error">
                      Username or Password Incorrect
                    </Typography>
                  )}

                  <TextField
                    required
                    type="email"
                    value={email}
                    onChange={updateEmail}
                    placeholder="Email"
                    fullWidth
                    InputProps={{
                      startAdornment: (
                        <InputAdornment position="start">
                          <EmailIcon />
                        </InputAdornment>
                      ),
                    }}
                  />

                  <TextField
                    required
                    type="password"
                    value={password}
                    onChange={updatePassword}
                    placeholder="Password"
                    fullWidth
                    InputProps={{
                      startAdornment: (
                        <InputAdornment position="start">
                          <LockIcon />
                        </InputAdornment>
                      ),
                    }}
                  />

                  <Button
                    type="submit"
                    variant="contained"
                    fullWidth
                    color="primary"
                    sx={{ fontWeight: "bold" }}
                  >
                    Login
                  </Button>
                </Stack>
              </form>
            </Grid>
          </Grid>
        </Paper>
      </Box>
    </>
  );
}
export default Login;
