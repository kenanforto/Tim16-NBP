import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { Button, TextField, Typography, Container, Box } from '@mui/material';
import { registerUser } from '../api/services/authService';
import hotelBg from '../assets/hotel-bg.jpeg';
import logo from '../assets/logo.png'

function Register() {
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [birthDate, setBirthDate] = useState('');
  const [addressId, setAddressId] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    try {
      await registerUser({ firstName, lastName, email, password, phoneNumber, birthDate, addressId: Number(addressId) });
      navigate('/login');
    } catch (err) {
      console.log('Register error:', err);
      setError('Registration failed. Please try again.');
    }
  };

  return (
    <Box
      sx={{
        position: 'relative',
        backgroundImage: `url(${hotelBg})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        '&::before': {
          content: '""',
          position: 'absolute',
          top: 0,
          left: 0,
          right: 0,
          bottom: 0,
          backgroundColor: 'rgba(255, 255, 255, 0.2)',
        },
      }}
    >
    <Container maxWidth="sm">
      <Box
        display="flex"
        flexDirection="column"
        justifyContent="center"
        alignItems="center"
        minHeight="100vh"
      >
       
        <Container maxWidth="sm" sx={{ bgcolor: 'rgba(255, 255, 255, 0.65)', borderRadius: 2, p: 4, display: 'flex', flexDirection:'column', justifyContent:'center', alignItems:'center' }}>
        <Typography variant="h5" component="h1" gutterBottom>
          <img src={logo} alt="Logo" style={{ height: '50px' }} />
        </Typography>
        <form onSubmit={handleRegister} style={{ width: '100%' }}>
          <TextField
            label="First Name"
            variant="outlined"
            fullWidth
            margin="normal"
            value={firstName}
            onChange={(e) => setFirstName(e.target.value)}
            required
          />
          <TextField
            label="Last Name"
            variant="outlined"
            fullWidth
            margin="normal"
            value={lastName}
            onChange={(e) => setLastName(e.target.value)}
            required
          />
          <TextField
            label="Email"
            type="email"
            variant="outlined"
            fullWidth
            margin="normal"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <TextField
            label="Password"
            type="password"
            variant="outlined"
            fullWidth
            margin="normal"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <TextField
            label="Phone Number"
            variant="outlined"
            fullWidth
            margin="normal"
            value={phoneNumber}
            onChange={(e) => setPhoneNumber(e.target.value)}
            required
          />
          <TextField
            label="Birth Date"
            type="date"
            variant="outlined"
            fullWidth
            margin="normal"
            value={birthDate}
            onChange={(e) => setBirthDate(e.target.value)}
            InputLabelProps={{ shrink: true }}
            required
          />
          <TextField
            label="Address ID"
            variant="outlined"
            fullWidth
            margin="normal"
            value={addressId}
            onChange={(e) => setAddressId(e.target.value)}
            required
          />
          {error && <Typography color="error">{error}</Typography>}
          <Button type="submit" variant="contained" color="primary" fullWidth sx={{backgroundColor: '#000'}}>
            Register
          </Button>
        </form>
        </Container>
        <Typography variant="body2" style={{ marginTop: '1rem' }}>
          Already have an account? <Link to="/login" style={{ color: 'black', fontWeight: 'bold' }}>Login here</Link>
        </Typography>
      </Box>
    </Container>
    </Box>
  );
}

export default Register;
