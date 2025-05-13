import axios from 'axios';

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
    const headers = response.headers;
    if (headers === undefined || headers === null) {
      return response;
    }
    const authHeader = headers['api-token'];

    if (authHeader) {
      const token = authHeader.split(' ')[1]; // Extract token from 'Bearer <token>'
      localStorage.setItem('token', token);
    }
    return response;
  },
  (error) => {
    if (error.response && error.response.status === 401) {
      console.error('Unauthorized access', error);
      if (window.location.pathname !== '/login') {
        console.log('Redirecting to login page');
        window.location.href = '/login';
        console.log('Redirected to login page', error);
      }
    }
    return Promise.reject(error);
  }
);

export default instance;
