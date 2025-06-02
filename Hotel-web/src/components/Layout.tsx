import { Outlet, Link, useNavigate } from 'react-router-dom';
import { AppBar, Toolbar, Typography, Button, Container } from '@mui/material';
import logo from '../assets/logo.png';

function Layout() {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  return (
    <>
      <AppBar position="static">
        <Toolbar sx={{
          backgroundColor: '#e7d9b9',
        }}>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            <img src={logo} alt="Logo" style={{ height: '32px' }} />
          </Typography>
          <Button component={Link} to="/" sx={{color:'#000'}}>
            Home
          </Button>
          <Button component={Link} to="/rooms"  sx={{color:'#000'}}>
            Rooms
          </Button>
          <Button onClick={handleLogout}  sx={{color:'#000'}}>
            Logout
          </Button>
        </Toolbar>
      </AppBar>
      <Container>
        <Outlet />
      </Container>
    </>
  );
}

export default Layout;
