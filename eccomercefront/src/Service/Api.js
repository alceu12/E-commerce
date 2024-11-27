
import axios from 'axios';
import qs from 'qs';

const api = axios.create({
  baseURL: 'https://e-commerce-07vh.onrender.com/',
  paramsSerializer: params => qs.stringify(params, { arrayFormat: 'repeat' }),
});

export default api;
