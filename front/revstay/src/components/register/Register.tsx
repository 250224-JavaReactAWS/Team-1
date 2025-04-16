import axios from "axios";
import { ChangeEvent, SyntheticEvent, useContext, useState } from "react";
import { IUser } from "../../interfaces/IUser";
import { 
  Box,
  Button,
  Grid,
  MenuItem,
  Paper,
  Select,
  FormControl,
  InputLabel,
  TextField,
  Typography,
  InputAdornment,
  SelectChangeEvent,
 } from "@mui/material";
import { authContext } from "../../App";
import logo from "../../assets/react.svg";
import { useNavigate } from "react-router";
import EmailIcon from '@mui/icons-material/Email';
import AssignmentIndIcon from '@mui/icons-material/AssignmentInd';
import LockIcon from '@mui/icons-material/Lock';


function Register() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [full_name, setFullName] = useState("");
  const [role, setRole] = useState("");
  const [error, setError] = useState(false);

  const navigate = useNavigate();
  const roleReference = useContext(authContext);

  const updateFullName = (e: ChangeEvent<HTMLInputElement>) => {
    setFullName(e.target.value);
  };
  
  const updateEmail = (e: ChangeEvent<HTMLInputElement>) => {
    setEmail(e.target.value);
  };

  const updatePassword = (e: ChangeEvent<HTMLInputElement>) => {
    setPassword(e.target.value);
  };

  const updateRole = (e: SelectChangeEvent<string>) => {
    setRole(e.target.value);
  };

  let register = async () => {
    try {
      let res = await axios.post<IUser>(
        "http://localhost:8080/users/register",
        {
          fullName: full_name,
          email: email,
          passwordHash: password,
          userType: role,
        },
        { withCredentials: true }
      );

      console.log(res);
      roleReference?.setRole(res.data.userType);
      navigate("/login");
    } catch (error) {
      setError(true);
      console.log(error);
      roleReference?.setRole("UNAUTHENTICATED");
    }
  };

  let submitForm = async (e: SyntheticEvent<HTMLFormElement>) => {
    e.preventDefault();

    register();
  };

  // return (
  //   <Box
  //     component="form"
  //     onSubmit={submitForm}
  //     sx={{
  //       display: "flex",
  //       flexDirection: "column",
  //       alignItems: "center",
  //       gap: 2,
  //       width: "100%",
  //       maxWidth: 400,
  //       margin: "0 auto",
  //       padding: 2,
  //       border: "1px solid #ccc",
  //       borderRadius: 4,
  //       boxShadow: "0 2px 4px rgba(0, 0, 0, 0.1)",
  //     }}
  //   >
  //     <Typography variant="h4" component="h1" gutterBottom>
  //       Register
  //     </Typography>
  //     {error && (
  //       <Typography color="error" variant="body2">
  //         Registration failed. Please try again.
  //       </Typography>
  //     )}
  //     <TextField
  //       label="Full Name"
  //       value={full_name}
  //       onChange={updateFullName}
  //       fullWidth
  //       required
  //     />
  //     <TextField
  //       label="Email"
  //       type="email"
  //       value={email}
  //       onChange={updateEmail}
  //       fullWidth
  //       required
  //     />
  //     <TextField
  //       label="Password"
  //       type="password"
  //       value={password}
  //       onChange={updatePassword}
  //       fullWidth
  //       required
  //     />
  //     <FormControl fullWidth required>
  //       <InputLabel>Role</InputLabel>
  //       <Select value={role} onChange={updateRole}>
  //         <MenuItem value="OWNER">Owner</MenuItem>
  //         <MenuItem value="USER">User</MenuItem>
  //       </Select>
  //     </FormControl>
  //     <Button type="submit" variant="contained" color="primary" fullWidth>
  //       Register
  //     </Button>
  //   </Box>
  // );

  
  return (
    <>
      <Box
      sx={{
        position: "fixed",
        top: '5%',
        left: 0,
        width: "100%",
        height: "100%",
        background: "linear-gradient(135deg, #3f51b5 0%,rgb(255, 81, 0) 100%)",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        zIndex: -1,

      }}
    />
    <Box
        sx={{
          position: 'relative',
          width: '100%',
          height: '100%',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
        }}
      >
      <Paper
        elevation={6}
        sx={{
          
          width: { xs: "96%", md: "90vh" },
          height: "min(500px, 80vh)",
          borderRadius: 4,
          overflow: "hidden",
        }}
      >
        <Grid container sx={{ height: "100%" }}>
          {/* Logo Side */}
          <Grid
            size={4}
            sx={{
              backgroundColor: "#f5f5f5",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              height: "100%",
              p: 4,
            }}
          >
            <Box component="img" src={logo} alt="Logo" sx={{ width: '50%', maxWidth: 180 }} />
          </Grid>

          {/* Form Side */}
          <Grid
            size={6}
            sx={{
              margin: '0 auto',
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              height: "100%",
              p: 2,
            }}
          >
            <Box
              component="form"
              onSubmit={submitForm}
              sx={{
                display: "flex",
                flexDirection: "column",
                gap: 2,
                width: "100%",
                maxWidth: 350,
              }}
            >
              <Typography variant="h4" fontWeight="bold" textAlign="center">
                Register
              </Typography>

              {error && (
                <Typography color="error" variant="body2">
                  Registration failed. Please try again.
                </Typography>
              )}

              <TextField
                required
                value={full_name}
                onChange={updateFullName}
                placeholder="Full name"
                fullWidth
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <AssignmentIndIcon />
                    </InputAdornment>
                  ),
                }}
              />
              <TextField
                type="email"
                value={email}
                onChange={updateEmail}
                fullWidth
                placeholder="Email"
                required
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <EmailIcon />
                    </InputAdornment>
                  ),
                }}
              />
              <TextField
                type="password"
                value={password}
                onChange={updatePassword}
                placeholder="Password"
                fullWidth
                required
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <LockIcon />
                    </InputAdornment>
                  ),
                }}
              />
              <FormControl fullWidth required>
                <InputLabel>Role</InputLabel>
                <Select value={role} onChange={updateRole} label="Role">
                  <MenuItem value="OWNER">Owner</MenuItem>
                  <MenuItem value="USER">User</MenuItem>
                </Select>
              </FormControl>
              <Button type="submit" variant="contained" color="primary" fullWidth
                sx={{fontWeight:'bold' }}>
                Register
              </Button>
            </Box>
          </Grid>
        </Grid>
      </Paper>
    </Box>
    </>
  );

}

export default Register;
