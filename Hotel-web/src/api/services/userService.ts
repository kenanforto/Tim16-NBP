import axios from '../axios';

export const getAllUsers = () => {
  return axios.get('/api/users');
};

export const getUserByUsername = (username: string) => {
  return axios.get(`/api/users/${username}`);
};
