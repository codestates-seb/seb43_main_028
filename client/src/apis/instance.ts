import axios from 'axios'
import { getAccessTokenFromLocalStorage } from '../utils/accessTokenHandler'

export const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_ENDPOINT,
  timeout: 3000,
  headers: {
    Authorization: getAccessTokenFromLocalStorage(),
    'Content-Type': 'application/json',
  },
  withCredentials: true,
})

export const fileAxios = axios.create({
  baseURL: import.meta.env.VITE_API_ENDPOINT,
  timeout: 3000,
  headers: {
    Authorization: getAccessTokenFromLocalStorage(),
    'Content-Type': 'multipart/form-data',
  },
  withCredentials: true,
})

