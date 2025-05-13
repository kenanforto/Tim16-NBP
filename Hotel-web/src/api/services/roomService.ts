import axios from '../axios';
import type { RoomRequest } from '../types';

export const getRoomById = (id: number) => {
  return axios.get(`/api/room/${id}`);
};

export const createRoom = (data: RoomRequest) => {
  return axios.post('/api/room', data);
};

export const updateRoom = (id: number, data: RoomRequest) => {
  return axios.put(`/api/room/${id}`, data);
};

export const deleteRoom = (id: number) => {
  return axios.delete(`/api/room/${id}`);
};
