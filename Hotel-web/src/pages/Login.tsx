import { useState } from 'react';
import { loginUser } from '../api/services/authService';
import axios from 'axios';
import { useNavigate, Link } from 'react-router-dom';
import { Button, TextField, Typography, Container, Box } from '@mui/material';

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
    <Container maxWidth="sm">
      <Box
        display="flex"
        flexDirection="column"
        justifyContent="center"
        alignItems="center"
        minHeight="100vh"
      >
        <Typography variant="h4" component="h1" gutterBottom>
          Hotel System
        </Typography>
        <form onSubmit={handleLogin} style={{ width: '100%' }}>
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
          <Button type="submit" variant="contained" color="primary" fullWidth>
            Login
          </Button>
        </form>
        <Typography variant="body2" style={{ marginTop: '1rem' }}>
          Don’t have an account? <Link to="/register">Register here</Link>
        </Typography>
      </Box>
    </Container>
  );
}

export default Login;
