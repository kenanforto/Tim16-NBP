import axios from '../axios';
import type { RegistrationRequest, AuthenticationRequest } from '../types';

export const registerUser = (data: RegistrationRequest) => {
  return axios.post('/api/auth/register', data);
};

export const loginUser = (data: AuthenticationRequest) => {
  return axios.post('/api/auth/login', data);
};

export const getCurrentUser = () => {
  return axios.get('/api/auth/me');
};
