import axios from '../axios';
import type { RoleRequest } from '../types';

export const getAllRoles = () => {
  return axios.get('/api/roles');
};

export const getRoleById = (id: number) => {
  return axios.get(`/api/roles/${id}`);
};

export const createRole = (data: RoleRequest) => {
  return axios.post('/api/roles', data);
};

export const updateRole = (id: number, data: RoleRequest) => {
  return axios.put(`/api/roles/${id}`, data);
};

export const deleteRole = (id: number) => {
  return axios.delete(`/api/roles/${id}`);
};
