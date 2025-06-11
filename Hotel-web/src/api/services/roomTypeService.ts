import axios from '../axios';
import type { RoomTypeRequest } from '../types';

export const getRoomTypeById = (id: number) => {
  return axios.get(`/api/room-type/${id}`);
};

export const getAllRoomTypes = () => {
  return axios.get(`/api/room-type`);
};

export const createRoomType = (data: RoomTypeRequest) => {
  return axios.post('/api/room-type', data);
};

export const updateRoomType = (id: number, data: RoomTypeRequest) => {
  return axios.put(`/api/room-type/${id}`, data);
};

export const deleteRoomType = (id: number) => {
  return axios.delete(`/api/room-type/${id}`);
};
