import axios from "axios";
import { ChangeEvent, SyntheticEvent, useContext, useState } from "react";
import { IUser } from "../../interfaces/IUser";
import { Box, Button, TextField, Typography, Select, MenuItem, FormControl, InputLabel, SelectChangeEvent } from "@mui/material";
import { authContext } from "../../App";
import { useNavigate } from "react-router";

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
      roleReference?.setRole(res.data.role);
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

  return (
    <Box
      component="form"
      onSubmit={submitForm}
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        gap: 2,
        width: "100%",
        maxWidth: 400,
        margin: "0 auto",
        padding: 2,
        border: "1px solid #ccc",
        borderRadius: 4,
        boxShadow: "0 2px 4px rgba(0, 0, 0, 0.1)",
      }}
    >
      <Typography variant="h4" component="h1" gutterBottom>
        Register
      </Typography>
      {error && (
        <Typography color="error" variant="body2">
          Registration failed. Please try again.
        </Typography>
      )}
      <TextField
        label="Full Name"
        value={full_name}
        onChange={updateFullName}
        fullWidth
        required
      />
      <TextField
        label="Email"
        type="email"
        value={email}
        onChange={updateEmail}
        fullWidth
        required
      />
      <TextField
        label="Password"
        type="password"
        value={password}
        onChange={updatePassword}
        fullWidth
        required
      />
      <FormControl fullWidth required>
        <InputLabel>Role</InputLabel>
        <Select value={role} onChange={updateRole}>
          <MenuItem value="OWNER">Owner</MenuItem>
          <MenuItem value="USER">User</MenuItem>
        </Select>
      </FormControl>
      <Button type="submit" variant="contained" color="primary" fullWidth>
        Register
      </Button>
    </Box>
  );
}

export default Register;
