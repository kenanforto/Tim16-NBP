import React, { useEffect, useState } from 'react';
import { Container, Typography, Card, CardContent, CardActions, Button } from '@mui/material';
import Grid from '@mui/material/Grid';
import { useSnackbar } from 'notistack';
import * as roomService from '../api/services/roomService';
import type { Room } from '../api/types';

const Rooms: React.FC = () => {
  const [rooms, setRooms] = useState<Room[]>([]);
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

  useEffect(() => {
    const fetchRooms = async () => {
      try {
        const response = await roomService.getAllRooms();
        setRooms(response.data.elements);
      } catch (error) {
        console.error('Error fetching rooms:', error);
      }
    };

    fetchRooms();
  }, []);

  return (
    <Container>
      <Typography variant="h4" gutterBottom style={{ marginTop: '40px', marginBottom: '40px' }}>
        Available Rooms
      </Typography>
      <Grid container spacing={3}>
        {rooms.map((room) => (
          <Grid size={{xs:12, sm:6, md:4}}  key={room.id.toString()}>
            <Card>
              <CardContent>
                <Typography variant="h6">{room.description}</Typography>
                <Typography color="textSecondary">Floor: {room.floor}</Typography>
                <Typography color="textSecondary">Price: {room.price}KM</Typography>
              </CardContent>
              <CardActions>
                <Button
                  size="small"
                  color="primary"
                  onClick={() => handleBookNow(room.id)}
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
