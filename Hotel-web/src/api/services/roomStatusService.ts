import axios from '../axios';
import type { RoomStatusRequest } from '../types';

export const getRoomStatusById = (id: number) => {
  return axios.get(`/api/room-status/${id}`);
};

export const createRoomStatus = (data: RoomStatusRequest) => {
  return axios.post('/api/room-status', data);
};

export const updateRoomStatus = (id: number, data: RoomStatusRequest) => {
  return axios.put(`/api/room-status/${id}`, data);
};

export const deleteRoomStatus = (id: number) => {
  return axios.delete(`/api/room-status/${id}`);
};
