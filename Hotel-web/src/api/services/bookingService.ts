import axios from '../axios';
import type { BookingRequest } from '../types';

export const getAllBookings = () => {
  return axios.get('/api/bookings');
};

export const createBooking = (data: BookingRequest) => {
  return axios.post('/api/bookings', data);
};

export const deleteBooking = (id: number) => {
  return axios.delete(`/api/bookings/${id}`);
};
