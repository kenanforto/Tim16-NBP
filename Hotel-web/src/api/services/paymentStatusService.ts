import axios from '../axios';
import type { PaymentStatusRequest } from '../types';

export const getAllPaymentStatuses = () => {
  return axios.get('/api/payment-status');
};

export const createPaymentStatus = (data: PaymentStatusRequest) => {
  return axios.post('/api/payment-status', data);
};

export const updatePaymentStatus = (id: number, data: PaymentStatusRequest) => {
  return axios.put(`/api/payment-status/${id}`, data);
};

export const deletePaymentStatus = (id: number) => {
  return axios.delete(`/api/payment-status/${id}`);
};
