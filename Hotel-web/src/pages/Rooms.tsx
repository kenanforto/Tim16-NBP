import React, { useEffect } from 'react';
import { Container, Typography, Card, CardContent, CardActions, Button, CardMedia, Chip, Box } from '@mui/material';
import Grid from '@mui/material/Grid';
import { useSnackbar } from 'notistack';
import { useRooms } from '../context/RoomContext';
import bgImg from '../assets/hotel-bg.jpeg'

const Rooms: React.FC = () => {
  const { enqueueSnackbar } = useSnackbar();

  const handleBookNow = async (roomId: number) => {
    try {
      console.log('Booking room with ID:', roomId);
      enqueueSnackbar('Room booked successfully!', { variant: 'success' });
    } catch (error) {
      console.error('Error booking room:', error);
      enqueueSnackbar('Failed to book the room. Please try again.', { variant: 'error' });
    }
  };

  const { rooms, refreshRooms } = useRooms();

  useEffect(() => {
    refreshRooms();
  }, []);

  return (
    <Container>
      <Typography variant="h4" gutterBottom style={{ marginTop: '40px', marginBottom: '40px' }}>
        Available Rooms
      </Typography>
      <Grid container spacing={3}>
        {rooms.map((room) => (
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
                      image={!room.image ? bgImg: undefined}
                      src={room.image ? `data:image/jpeg;base64,${room.image}`: undefined}
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
                      onClick={() => handleBookNow(room.id)}
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
        ))}
      </Grid>
    </Container>
  );
};

export default Rooms;
