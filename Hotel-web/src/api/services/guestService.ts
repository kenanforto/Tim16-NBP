import axios from '../axios';
import type { GuestRequest } from '../types';

export const getAllGuests = () => {
  return axios.get('/api/guests');
};

export const getGuestById = (id: number) => {
  return axios.get(`/api/guests/${id}`);
};

export const createGuest = (data: GuestRequest) => {
  return axios.post('/api/guests', data);
};

export const deleteGuest = (id: number) => {
  return axios.delete(`/api/guests/${id}`);
};
