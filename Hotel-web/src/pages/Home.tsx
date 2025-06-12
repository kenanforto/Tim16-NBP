import { useEffect, useState } from 'react';
import {
  Box,
  Grid,
  Card,
  CardMedia,
  CardContent,
  Typography,
  Chip,
  Button,
  Drawer,
  Container,
  CardActions
} from '@mui/material';
import { Link } from 'react-router-dom';
import logo from '../assets/logoWhite.png';
import bgVideo from '../assets/bgVideo.mp4';
import { useRooms } from '../context/RoomContext';
import bgImg from '../assets/hotel-bg.jpeg'
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import type { Room } from '../types/room';
import type { PickerValue } from '@mui/x-date-pickers/internals';


// type Room = {
//   id: number;
//   name: string;
//   floor: number;
//   price: number;
// };

function Home() {

  const [selectedRoom, setSelectedRoom] = useState<Room | null>(null);
  const [drawerOpen, setDrawerOpen] = useState(false);
  const [fromDate, setFromDate] = useState<PickerValue | undefined>(undefined);
  const [toDate, setToDate] = useState<PickerValue | undefined>(undefined);

  const { rooms, refreshRooms } = useRooms();

  useEffect(() => {
    refreshRooms();
  }, []);

  const handleOpenDrawer = (room: Room) => {
    setSelectedRoom(room);
    setDrawerOpen(true);
  };

  const handleCloseDrawer = () => {
    setDrawerOpen(false);
    setSelectedRoom(null);
    setFromDate(null);
    setToDate(null);
  };


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

        <Box
          sx={{
            position: 'relative',
            zIndex: 1,
            textAlign: 'center',
          }}
        >
          <img src={logo} alt="TranquilStays Logo" style={{ width: 320, maxWidth: '80%' }} />
        </Box>

        <Box
          sx={{
            position: 'absolute',
            top: 0,
            left: 0,
            width: '100%',
            height: '100%',
            background: 'rgba(0,0,0,0.3)',
            zIndex: 1,
          }}
        />
      </Box>

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
          {rooms.map(room => {
            return (
              <Grid key={room.id} size={{ xs: 12, sm: 6, md: 4 }}>
                <Card
                  sx={{
                    position: 'relative',
                    height: '100%',
                    overflow: 'hidden',
                    boxShadow: 1,
                    transition: 'transform 0.3s ease, box-shadow 0.3s ease',
                    '&:hover': {
                      transform: 'translateY(-6px)',
                      boxShadow: 3,
                      cursor: 'pointer',
                    },
                  }}
                >
                  <Box position="relative">
                    <CardMedia
                      component="img"
                      height="200"
                      // image={roomImages[room.id] || bgImg}
                      image={!room.image ? bgImg : undefined}
                      src={room.image ? `data:image/jpeg;base64,${room.image}` : undefined}
                      alt={room.description || `Room #${room.id}`} />
                    <Chip
                      label={`$ ${room.price || 'N/A'}`}
                      sx={{
                        borderRadius: 0,
                        position: 'absolute',
                        top: 12,
                        right: 12,
                        color: '#d6d6d6',
                        border: '1px solid #d6d6d6'
                      }} />
                  </Box>
                  <CardContent>
                    <Typography variant="h6" fontWeight={600}>
                      {room.type ? room.type.description : `Room____ #${room.id}`}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      Floor: {room.floor}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      {room.description}
                    </Typography>
                  </CardContent>
                  <CardActions disableSpacing>
                    <Button
                      variant="contained"
                      size="small"
                      style={{ marginLeft: 'auto' }}
                      onClick={() => handleOpenDrawer(room)}
                      sx={{
                        backgroundColor: 'transparent',
                        border: '1px solid #6f7170',
                        color: '#919393',
                        boxShadow: 'none',
                        opacity: 0,
                        borderRadius: 0,
                        '&:hover': {
                          backgroundColor: '#717372',
                          color: '#fff',
                        },
                        '.MuiCard-root:hover &': {
                          opacity: 1,
                          pointerEvents: 'auto',
                        },
                      }}
                    >
                      Book Now
                    </Button>
                  </CardActions>
                </Card>
              </Grid>
            );
          })}
        </Grid>

      </Container>

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

        <Typography variant="caption" color="text.secondary" mt={2} ml={2}>
          Cancel anytime. No hidden fees.
        </Typography>
      </Box>


      {/* Gallery */}
      <Box bgcolor="#f4f4f4" px={2} py={8}>
        <Grid container spacing={3} justifyContent="center">
          {rooms.slice(0, 8).map((room, index) => (
            <Grid key={index}>
              <Card sx={{ boxShadow: 2 }}>
                <CardMedia
                  component="img"
                  height="200"
                  image={room.image ? `data:image/jpeg;base64,${room.image}` : bgImg}
                  alt={`Room ${room.id}`}
                  sx={{ objectFit: 'cover' }}
                />
              </Card>
            </Grid>
          ))}
        </Grid>

        <Box
          maxWidth="600px"
          mx="auto"
          textAlign="center"
          bgcolor="white"
          p={4}
          mt={6}
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

      <Drawer
        anchor="right"
        open={drawerOpen}
        onClose={handleCloseDrawer}
        PaperProps={{
          sx: { width: 350, p: 2 },
        }}
      >
        {selectedRoom && (
          <>
            <Box mb={2}>
              <CardMedia
                component="img"
                height="160"
                image={!selectedRoom.image ? bgImg : undefined}
                src={selectedRoom.image ? `data:image/jpeg;base64,${selectedRoom.image}` : undefined}
                alt="Room Preview"
                sx={{ borderRadius: 2 }}
              />
            </Box>
            <Typography variant="h5" fontWeight={600}>
              {selectedRoom.type.description}
            </Typography>
            <Typography variant="h6" fontWeight={400} mb={1}>
              {selectedRoom.description}
            </Typography>
            <Typography variant="body2" color="text.secondary" mb={1}>
              Floor: {selectedRoom.floor}
            </Typography>
            <Typography variant="body1" mb={2}>
              ${selectedRoom.price}
            </Typography>

            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <DatePicker
                label="From Date"
                value={fromDate}
                onChange={(newDate) => setFromDate(newDate)}
                sx={{ mb: 2 }}
              />
              <DatePicker
                label="To Date"
                value={toDate}
                onChange={(newDate) => setToDate(newDate)}
              />
            </LocalizationProvider>

            <Button
              fullWidth
              variant="contained"
              sx={{
                mt: 2,
                backgroundColor: 'transparent',
                border: '1px solid #6f7170',
                color: '#919393',
                boxShadow: 'none',
                opacity: 1,
                borderRadius: 0,
                '&:hover': {
                  backgroundColor: '#717372',
                  color: '#fff',
                },
              }}
              onClick={() => alert(`Booking room "${selectedRoom.description}" from ${fromDate} to ${toDate}`)}
            >
              Confirm Booking
            </Button>
          </>
        )}
      </Drawer>
    </Box>
  );
}

export default Home;
