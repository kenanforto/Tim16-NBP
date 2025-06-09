import { Outlet, Link, useNavigate } from 'react-router-dom';
import { AppBar, Toolbar, Button, Container, Box } from '@mui/material';
import logo from '../assets/logo.png';

function Layout() {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  return (
    <>
      <AppBar position="static" elevation={0} sx={{ backgroundColor: '#f6f1e9' }}>
        <Toolbar>
          <Box sx={{ flexGrow: 1 }}>
            <Link to="/" style={{ display: 'flex', alignItems: 'center', textDecoration: 'none' }}>
              <img src={logo} alt="Logo" style={{ height: 36, marginRight: 8 }} />
            </Link>
          </Box>
          <Button component={Link} to="/" sx={{ color: '#000', textTransform: 'none' }}>
            Home
          </Button>
          <Button component={Link} to="/rooms" sx={{ color: '#000', textTransform: 'none' }}>
            Rooms
          </Button>
          <Button onClick={handleLogout} sx={{ color: '#000', textTransform: 'none' }}>
            Logout
          </Button>
        </Toolbar>
      </AppBar>
      <Outlet />
    </>
  );
}

export default Layout;
