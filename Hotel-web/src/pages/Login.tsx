import { useState } from 'react';
import { loginUser } from '../api/services/authService';
import axios from 'axios';
import { useNavigate, Link } from 'react-router-dom';
import { Button, TextField, Typography, Container, Box } from '@mui/material';
import hotelBg from '../assets/hotel-bg.jpeg';
import logo from '../assets/logo.png'

function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    try {
      await loginUser({ username, password });
      navigate('/');
    } catch (err) {
      console.log('Login error:', err);
      if (axios.isAxiosError(err) && err.response) {
        setError(err.response.data?.error || 'Login failed. Please try again.');
      } else {
        setError('An unexpected error occurred. Please try again.');
      }
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
      <Container maxWidth="sm" sx={{ bgcolor: 'rgba(255, 255, 255, 0.65)', borderRadius: 2, p: 4 }}>
        <Typography variant="h4" component="h1" gutterBottom align="center">
          <img src={logo} alt="Logo" style={{ height: '50px' }} />
        </Typography>
        <form onSubmit={handleLogin}>
          <TextField
            label="Username"
            variant="outlined"
            fullWidth
            margin="normal"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
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
          {error && <Typography color="error">{error}</Typography>}
          <Button type="submit" variant="contained" fullWidth sx={{backgroundColor: '#000'}}>
            Login
          </Button>
        </form>
        <Typography variant="body2" align="center" sx={{ mt: 2 }}>
          Don’t have an account? 
          <Link to="/register" style={{ color: 'black', fontWeight: 'bold' }}>
          Register here
          </Link>
        </Typography>
      </Container>
    </Box>
  );
}

export default Login;
