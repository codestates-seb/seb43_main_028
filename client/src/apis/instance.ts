import axios, { AxiosRequestConfig, InternalAxiosRequestConfig } from 'axios'
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

axiosInstance.interceptors.response.use(
  config => {
    const accessToken = getAccessTokenFromLocalStorage()
    // eslint-disable-next-line no-param-reassign
    config.headers = {
      Authorization: accessToken || '',
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

export const authInstance = axios.create({
  baseURL: import.meta.env.VITE_API_ENDPOINT,
  timeout: 3000,
  headers: {
    Authorization: getAccessTokenFromLocalStorage(),
    'Content-Type': 'application/json',
  },
  withCredentials: true,
})

authInstance.interceptors.response.use(
  response => response,
  async error => {
    const {
      config,
      response: { status },
    } = error

    if (status === 401) {
      const originalRequest = config
      const refreshRes = await axiosInstance.get('/members/refresh')
      if (refreshRes.status === 200) {
        return axios(originalRequest)
      }
    }
    return Promise.reject(error)
  }
)
