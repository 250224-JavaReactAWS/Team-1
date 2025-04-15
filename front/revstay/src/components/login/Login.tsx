import axios from "axios"
import { ChangeEvent, SyntheticEvent, useContext, useState } from "react"
import { IUser } from "../../interfaces/IUser"
import { Box, Button, InputAdornment, Paper, Stack, TextField, Typography } from "@mui/material";
import { authContext } from "../../App"
import { useNavigate } from "react-router"
import EmailIcon from '@mui/icons-material/Email';
import LockIcon from '@mui/icons-material/Lock';
import Grid from '@mui/material/Grid';


function Login() {
  /*
  Inside of this class I want to send an axios request to login my user so let's do that
  */

  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState(false)

  // Let's use the context to get the role functions
  const roleReference = useContext(authContext)

  // Whenever we log in lets nav to the courses page
  const navigate = useNavigate();

  let updateEmail = (e: ChangeEvent<HTMLInputElement>) => {
    setEmail(e.target.value)
  }

  let updatePassword = (e: ChangeEvent<HTMLInputElement>) => {
    setPassword(e.target.value)
  }

  let login = async () => {
    // This function should make our axios request
    try{
      let res = await axios.post<IUser>('http://localhost:8080/users/login',
        // Data is the body of our request
        {
            email: email,
            passwordHash: password // Cambia el nombre de la propiedad
        },
        // The next option is for a config object
        {withCredentials: true} // This allows for the JSESSIONID to be passed
      )

      console.log(res)
      roleReference?.setRole(res.data.userType);
      localStorage.setItem('user', JSON.stringify(res.data));
      navigate('/')
    } catch (error){
      setError(true)
      console.log(error)
      roleReference?.setRole("UNAUTHENTICATED")
    }
  }

  let submitForm = async (e: SyntheticEvent<HTMLFormElement>) => {
    e.preventDefault() // This prevents the form from clearing until we want it to

    login()
  }

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
          position: 'fixed',
          top: 0,
          left: 0,
          width: '100vw',
          height: '100vh',
          background: 'linear-gradient(135deg, #3f51b5 0%, #e91e63 100%)',
          zIndex: -1,
        }}
      />
      <Box
        sx={{
          position: 'relative',
          width: '100vw',
          height: '100vh',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
        }}
      >
        <Paper
        elevation={6}
        sx={{
          width: { xs: '90%', md: '35%' },
          borderRadius: 4,
          overflow: 'hidden',
          zIndex: 1, // Para que el login esté por delante
        }}
      >
          <Grid container>
          <Grid 
            item
            xs={12}
            md={6}
            sx={{
              backgroundColor: '#f5f5f5',
              display: 'flex',
              justifyContent: 'center',
              alignItems: 'center',
              p: 4,
            }}
          >
              <Box
                component="img"
                src="src\assets\react.svg" 
                alt="Logo"
                sx={{ width: '100%', maxWidth: 180}}
              />
            </Grid>

            <Grid item xs={12} md={6} sx={{ p: 5 }}>
              <form onSubmit={submitForm}>
                <Stack spacing={3}>
                  <Typography variant="h4" fontWeight="bold" textAlign="center">
                    Login
                  </Typography>

                    {error && <Typography color="error">
                      Username or Password Incorrect
                    </Typography>}

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
                    sx={{ backgroundColor: '#4CAF50', fontWeight: 'bold' }}
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
export default Login