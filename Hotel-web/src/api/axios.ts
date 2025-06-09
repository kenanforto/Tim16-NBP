import axios from 'axios';
import { decodeJwtToken } from './util';
import type { User } from '../types/user';
import constants from '../constants';

const instance = axios.create({
  baseURL: 'http://localhost:8080',
});

instance.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token && config.headers) {
    config.headers.set('Authorization', `Bearer ${token}`);
  }
  return config;
});

instance.interceptors.response.use(
  (response) => {
    const statusCode = response.status;
    if (statusCode == 401) {
      localStorage.clear();
      return response;
    }
    const headers = response.headers;
    if (headers === undefined || headers === null) {
      return response;
    }
    const authHeader = headers['api-token'];

    if (authHeader) {
      const token = authHeader.split(' ')[1]; // Extract token from 'Bearer <token>'
      localStorage.setItem('token', token);
      const claims = decodeJwtToken<{ role: string; sub: string }>(token);
      const user = { role: claims?.role, email: claims?.sub } as User;
      console.log('User', user);
      // Broadcast user info to context
      window.postMessage({ type: 'SET_USER', user }, '*');
    }
    return response;
  },
  (error) => {
    if (error.response && error.response.status === 401) {
      window.postMessage({ type: 'LOGOUT_USER' }, '*');
      console.error('Unauthorized access', error);
      if (!constants.publicPaths.includes(window.location.pathname)) {
        console.log('Redirecting to login page');
        window.location.href = '/login';
        console.log('Redirected to login page', error);
      }
    }
    return Promise.reject(error);
  }
);

export default instance;
