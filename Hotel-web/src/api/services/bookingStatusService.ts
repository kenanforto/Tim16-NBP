import axios from '../axios';
import type { BookingStatusRequest } from '../types';

export const getAllBookingStatuses = () => {
  return axios.get('/api/booking-status');
};

export const createBookingStatus = (data: BookingStatusRequest) => {
  return axios.post('/api/booking-status', data);
};

export const updateBookingStatus = (id: number, data: BookingStatusRequest) => {
  return axios.put(`/api/booking-status/${id}`, data);
};

export const deleteBookingStatus = (id: number) => {
  return axios.delete(`/api/booking-status/${id}`);
};
