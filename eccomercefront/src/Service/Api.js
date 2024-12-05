import axios from 'axios';
import qs from 'qs';
import { jwtDecode } from 'jwt-decode';
import { Navigate } from 'react-router-dom';
const api = axios.create({
  baseURL: 'http://localhost:8080',
  paramsSerializer: params => qs.stringify(params, { arrayFormat: 'repeat' }),
});

// Função para verificar se o token é válido
const isTokenValid = () => {
  const token = localStorage.getItem('userToken');
  if (!token) {
    return false;
  }

  try {
    const decoded = jwtDecode(token);
    const currentTime = Date.now() / 1000; // Converter para segundos
    return decoded.exp && decoded.exp > currentTime;
  } catch (error) {
    return false;
  }
};

// Interceptor para verificar o token antes de cada requisição
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('userToken');
    if (token) {
      if (isTokenValid()) {
        config.headers.Authorization = `Bearer ${token}`;
      } else {
        localStorage.removeItem('jwtToken');
        console.log('Token expirado. Redirecionando para login...');
        <Navigate to="/login" />
        return Promise.reject(new Error('Token expirado'));
      }
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Interceptor para lidar com respostas de erro (opcional)
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && (error.response.status === 401 || error.response.status === 403)) {
      localStorage.removeItem('jwtToken');
      console.log('Não autorizado. Redirecionando para login...');
      // Opcional: redirecionar para a página de login
      // window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;
