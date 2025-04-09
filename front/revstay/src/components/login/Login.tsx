import axios from "axios"
import { ChangeEvent, SyntheticEvent, useContext, useState } from "react"
import { IUser } from "../../interfaces/IUser"
import { Box, Button, TextField, Typography } from "@mui/material"
import { authContext } from "../../App"
import { useNavigate } from "react-router"


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
            "email": "bob@example.com",
            "passwordHash" : "Password1"
        },
        // The next option is for a config object
        {withCredentials: true} // This allows for the JSESSIONID to be passed
      )

      console.log(res)
      roleReference?.setRole(res.data.role)
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

  return (

    <Box  
    sx={{ 
      border: '2px solid black',
      width: '100%',
      height: '50%',
      margin: 'auto',
      alignContent: 'center',
      textAlign: 'center'

    }}
    >
      <form onSubmit={submitForm} style={{margin: '20px'}}>
        <Typography variant='h2'>
          Login
        </Typography>

        {error && <Typography color="error">
          Username or Password Incorrect
        </Typography>}

        <TextField 
         required
          id="email-input" 
          label="Email" 
          variant="outlined" 
          value={email} 
          onChange={updateEmail} 
          type="email"
        />

        <br/>
        <br/>
        <br/>
        <TextField 
          required
          id="password-input" 
          label="Password" 
          variant="outlined" 
          value={password} 
          onChange={updatePassword} 
          type="password"
        />

        <br />
        <br />
        <br />

        {/* Forms behave slightly different than divs, we can use it for additional validation 
        or taking in input. It's important to note that we should prevent default to prevent the form from
        just clearing on submit */}
        <Button variant="contained" type="submit">Login!</Button>
      </form>
    </Box>
  )
}
export default Login