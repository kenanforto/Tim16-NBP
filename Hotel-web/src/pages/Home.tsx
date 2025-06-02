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
      {/* Hero Section */}
      <Container sx={{ py: 6 }}>
        <Box display="flex" justifyContent="space-between" alignItems="center" mb={4}>
          <Typography variant="h5" fontWeight={600}>
            Explore Best Rooms
          </Typography>
          <Stack direction="row" spacing={2}>
            <Button variant="contained" sx={{borderRadius:'22px', backgroundColor: '#587681'}}>
              View All →
            </Button>
          </Stack>
        </Box>

        <Grid container spacing={3}>
          {rooms.map((room, index) => (
            <Grid key={index}>
              <Card
                sx={{
                  width: 300,
                  display: 'flex',
                  flexDirection: 'column',
                  justifyContent: 'space-between',
                  position: 'relative',
                  borderRadius: 3,
                  cursor: 'pointer',
                  transition: 'transform 0.3s ease, box-shadow 0.3s ease',
                  '&:hover': {
                    transform: 'translateY(-8px)',
                    boxShadow: 6,
                    cursor: 'pointer'
                  },
                }}
              >
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
                    top: 16,
                    right: 16,
                    backgroundColor: 'white',
                    fontWeight: 'bold',
                  }}
                />
                <CardContent>
                  <Typography variant="caption" color="text.secondary">
                    {room.location}
                  </Typography>
                  <Typography variant="subtitle1" fontWeight={600}>
                    {room.name}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>


      </Container>

      {/* Promo Section */}
      <Box textAlign="center" bgcolor="#f9f9f9" py={10} px={2} sx={{
        display:'flex', flexDirection:'column', alignItems:'center'
      }}>
        <Typography variant="subtitle2" fontWeight="medium" mb={1}>
          Explore Rooms
        </Typography>
        <Typography variant="h4" fontWeight="bold" mb={2}>
          Discover luxurious room options
          <br />
          Book your perfect stay with ease
        </Typography>
        <Typography variant="body1" color="text.secondary" maxWidth="600px" mx="auto" mb={3}>
          Find the most comfortable and stylish rooms tailored to your travel needs.
        </Typography>
        <Button variant="contained" size="large" sx={{ borderRadius: '999px' }}>
         Explore Rooms →
        </Button>
        <Typography variant="caption" color="text.secondary" mt={2}>
          Cancel anytime. No hidden fees.
        </Typography>
      </Box>


      {/* Gallery + Community */}
      <Box bgcolor="#f9f9f9" px={2} py={8}>
        <Grid container spacing={2} mb={6} alignItems={'center'} justifyContent={'center'}>
          {[...Array(6)].map((_, i) => (
            <Grid key={i} sx={{display:'flex', flexDirection:'column', alignItems:'center'}}>
              <CardMedia
                component="img"
                image={rooms[i % rooms.length].image}
                alt={`room-${i}`}
                sx={{ borderRadius: 2, height: 160, objectFit: 'cover' }}
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
          <Typography color="text.secondary" mb={2}>
          Book your room and enjoy premium stays
          </Typography>
          <Button variant="contained" size="medium" sx={{ borderRadius: '999px' }}>
            Explore Rooms →
          </Button>
        </Box>
      </Box>
    </Box>
  );
}

export default Home;
