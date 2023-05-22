import axios from 'axios'

export const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_ENDPOINT,
  timeout: 3000,
  headers: {
    'Content-Type': 'application/json',
    'ngrok-skip-browser-warning': '69420',
  },
})

export const fileAxios = axios.create({
  baseURL: import.meta.env.VITE_API_ENDPOINT,
  timeout: 3000,
  headers: {
    'Content-Type': 'multipart/form-data',
  },
})
