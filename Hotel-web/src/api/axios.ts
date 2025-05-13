import axios from 'axios';

const instance = axios.create({
  baseURL: 'http://localhost:8080',
});

instance.interceptors.response.use(
  response => response, // Pass through successful responses
  error => {
    if (error.response && error.response.status === 401) {
      // Redirect to login page on 401 Unauthorized
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default instance;