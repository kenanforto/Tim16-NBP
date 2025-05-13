import axios from '../axios';
import type { RoomBookedRequest } from '../types';

export const getAllRoomBookings = () => {
  return axios.get('/api/room-booked');
};

export const createRoomBooking = (data: RoomBookedRequest) => {
  return axios.post('/api/room-booked', data);
};

export const updateRoomBooking = (id: number, data: RoomBookedRequest) => {
  return axios.put(`/api/room-booked/${id}`, data);
};

export const deleteRoomBooking = (id: number) => {
  return axios.delete(`/api/room-booked/${id}`);
};
