import axios from '../axios';
import type { PaymentRequest } from '../types';

export const getAllPayments = () => {
  return axios.get('/api/payments');
};

export const createPayment = (data: PaymentRequest) => {
  return axios.post('/api/payments', data);
};

export const deletePayment = (id: number) => {
  return axios.delete(`/api/payments/${id}`);
};
