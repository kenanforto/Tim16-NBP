import {
  Box,
  Typography,
  Card,
  CardMedia,
  CardContent,
  Chip,
  Button,
  Container,
  Stack,
  Grid,
} from '@mui/material';
import { Link } from 'react-router-dom';
import logo from '../assets/logo.png';
import bgVideo from '../assets/bgVideo.mp4';

const rooms = [
  {
    name: 'Deluxe Ocean View Room',
    price: '$320/Night',
    location: 'Land Side View',
    image: 'https://www.leonardo-hotels-cyprus.com/images/photos/photoGalleries/hotels/cypriaMaris/rooms/premiumRoomWithPanoramicSideSeaView/premiumRoomWithPanoramicSideSeaView_01.webp',
  },
  {
    name: 'Mountain Suite',
    price: '$280/Night',
    location: 'Sea Side View',
    image: 'https://www.leonardo-hotels-cyprus.com/images/photos/photoGalleries/hotels/cypriaMaris/rooms/premiumRoomWithPanoramicSideSeaView/premiumRoomWithPanoramicSideSeaView_01.webp',
  },
  {
    name: 'Modern Loft Room',
    price: '$200/Night',
    location: 'Land Side View',
    image: 'https://www.leonardo-hotels-cyprus.com/images/photos/photoGalleries/hotels/cypriaMaris/rooms/premiumRoomWithPanoramicSideSeaView/premiumRoomWithPanoramicSideSeaView_01.webp',
  },
  {
    name: 'Tropical Bungalow',
    price: '$350/Night',
    location: 'Sea Side View',
    image: 'https://www.leonardo-hotels-cyprus.com/images/photos/photoGalleries/hotels/cypriaMaris/rooms/premiumRoomWithPanoramicSideSeaView/premiumRoomWithPanoramicSideSeaView_01.webp',
  },
];

function Home() {
  return (
    <Box>

      <Box
        sx={{
          position: 'relative',
          height: '80vh',
          overflow: 'hidden',
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          backgroundColor: '#000'
        }}
      >
        {/* Background Video */}
        <video
          autoPlay
          muted
          loop
          playsInline
          style={{
            position: 'absolute',
            top: 0,
            left: 0,
            minWidth: '100%',
            minHeight: '100%',
            objectFit: 'cover',
            zIndex: 0,
          }}
        >
          <source src={bgVideo} type="video/mp4" />
          Your browser does not support the video tag.
        </video>

        {/* Centered Logo */}
        <Box
          sx={{
            position: 'relative',
            zIndex: 1,
            textAlign: 'center',
          }}
        >
          <img src={logo} alt="TranquilStays Logo" style={{ width: 180, maxWidth: '80%' }} />
        </Box>

        {/* Optional: Overlay */}
        <Box
          sx={{
            position: 'absolute',
            top: 0,
            left: 0,
            width: '100%',
            height: '100%',
            background: 'rgba(0,0,0,0.3)', // soft overlay
            zIndex: 1,
          }}
        />
      </Box>


      {/* Hero Section */}
      <Container sx={{ py: 6 }}>
        <Box display="flex" justifyContent="space-between" alignItems="center" mb={5}>
          <Typography variant="h5" fontWeight={600}>
            Explore Best Rooms
          </Typography>
          <Link to="/rooms" style={{ textDecoration: 'none' }}>
            <Button variant="contained"
              sx={{
                backgroundColor: 'transparent',
                border: '1px solid #6f7170',
                color: '#919393',
                boxShadow: 'none',
                borderRadius: 0,
                '&:hover': {
                  backgroundColor: '#717372',
                  color: '#fff'
                },
              }}
            >
              View All →
            </Button>
          </Link>
        </Box>

        <Grid container spacing={4}>
          {rooms.map((room, index) => (
            <Grid key={index}>
              <Card
                sx={{
                  height: '100%',
                  borderRadius: 4,
                  overflow: 'hidden',
                  boxShadow: 3,
                  transition: 'transform 0.3s ease, box-shadow 0.3s ease',
                  '&:hover': {
                    transform: 'translateY(-6px)',
                    boxShadow: 6,
                  },
                }}
              >
                <Box position="relative">
                  <CardMedia
                    component="img"
                    height="200"
                    image={room.image}
                    alt={room.name}
                  />
                  <Chip
                    label={room.price}
                    sx={{
                      position: 'absolute',
                      top: 12,
                      right: 12,
                      backgroundColor: '#fff',
                      fontWeight: 600,
                    }}
                  />
                </Box>
                <CardContent>
                  <Typography variant="body2" color="text.secondary">
                    {room.location}
                  </Typography>
                  <Typography variant="h6" fontWeight={600}>
                    {room.name}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      </Container>

      {/* Promo Section */}
      <Box textAlign="center" bgcolor="#fafafa" py={10} px={2}>
        <Typography variant="subtitle2" fontWeight={500} mb={1}>
          Explore Rooms
        </Typography>
        <Typography variant="h4" fontWeight={700} mb={2}>
          Discover luxurious room options
        </Typography>
        <Typography variant="body1" color="text.secondary" maxWidth="600px" mx="auto" mb={3}>
          Find the most comfortable and stylish rooms tailored to your travel needs.
        </Typography>
        <Link to="/rooms" style={{ textDecoration: 'none' }}>
          <Button variant="contained"
            sx={{
              backgroundColor: 'transparent',
              border: '1px solid #6f7170',
              color: '#919393',
              boxShadow: 'none',
              borderRadius: 0,
              '&:hover': {
                backgroundColor: '#717372',
                color: '#fff'
              },
            }}
          >
            Explore Rooms →
          </Button>
        </Link>
        <Typography variant="caption" color="text.secondary" mt={2}>
          Cancel anytime. No hidden fees.
        </Typography>
      </Box>

      {/* Gallery + Community */}
      <Box bgcolor="#f4f4f4" px={2} py={8}>
        <Grid container spacing={2} mb={6} justifyContent="center">
          {[...Array(6)].map((_, i) => (
            <Grid key={i}>
              <CardMedia
                component="img"
                image={rooms[i % rooms.length].image}
                alt={`room-${i}`}
                sx={{ borderRadius: 3, height: 160, width: 240, objectFit: 'cover' }}
              />
            </Grid>
          ))}
        </Grid>

        <Box
          maxWidth="600px"
          mx="auto"
          textAlign="center"
          bgcolor="white"
          p={4}
          borderRadius={3}
          boxShadow={3}
        >
          <Typography variant="h6" fontWeight={600} mb={1}>
            Book Today!
          </Typography>
          <Typography color="text.secondary" mb={3}>
            Book your room and enjoy premium stays
          </Typography>
          <Link to="/rooms" style={{ textDecoration: 'none' }}>
            <Button variant="contained"
              sx={{
                backgroundColor: 'transparent',
                border: '1px solid #6f7170',
                color: '#919393',
                boxShadow: 'none',
                borderRadius: 0,
                '&:hover': {
                  backgroundColor: '#717372',
                  color: '#fff'
                },
              }}
            >
              Explore Rooms →
            </Button>
          </Link>
        </Box>
      </Box>
    </Box>
  );
}

export default Home;
