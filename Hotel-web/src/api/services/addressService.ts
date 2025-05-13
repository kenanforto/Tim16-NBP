import axios from '../axios';
import type { AddressRequest } from '../types';

export const getAllAddresses = () => {
  return axios.get('/api/addresses');
};

export const getAddressById = (id: number) => {
  return axios.get(`/api/addresses/${id}`);
};

export const createAddress = (data: AddressRequest) => {
  return axios.post('/api/addresses', data);
};

export const deleteAddress = (id: number) => {
  return axios.delete(`/api/addresses/${id}`);
};
