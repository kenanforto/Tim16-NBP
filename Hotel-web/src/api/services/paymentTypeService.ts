import axios from '../axios';
import type { PaymentTypeRequest } from '../types';

export const getAllPaymentTypes = () => {
  return axios.get('/api/payment-type');
};

export const createPaymentType = (data: PaymentTypeRequest) => {
  return axios.post('/api/payment-type', data);
};

export const updatePaymentType = (id: number, data: PaymentTypeRequest) => {
  return axios.put(`/api/payment-type/${id}`, data);
};

export const deletePaymentType = (id: number) => {
  return axios.delete(`/api/payment-type/${id}`);
};
