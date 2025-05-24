import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api', // Adjust if needed
  withCredentials: true,
});

export default api;
