/* eslint-disable no-param-reassign */
import axios, { InternalAxiosRequestConfig } from 'axios'
import {
  getAccessTokenFromLocalStorage,
  saveAccessTokenToLocalStorage,
} from '../utils/accessTokenHandler'

const instanceOptions = {
  baseURL: import.meta.env.VITE_API_ENDPOINT,
  timeout: 3000,
  headers: { 'Content-Type': 'application/json', 'ngrok-skip-browser-warning': '69420' },
  withCredentials: true,
}

const setAccessTokenOnHeader = (config: InternalAxiosRequestConfig) => {
  config.headers.Authorization = getAccessTokenFromLocalStorage() || ''
  return config
}

function createAxiosInstance() {
  const instance = axios.create(instanceOptions)
  instance.interceptors.request.use(setAccessTokenOnHeader)
  return instance
}

function createFileAxiosInstance() {
  const instance = axios.create({
    ...instanceOptions,
    headers: { 'Content-Type': 'multipart/form-data' },
  })
  instance.interceptors.request.use(setAccessTokenOnHeader)
  return instance
}

function createAuthAxiosInstance() {
  const instance = axios.create(instanceOptions)
  instance.interceptors.request.use(setAccessTokenOnHeader)
  instance.interceptors.response.use(
    response => response,
    async error => {
      const { config, response } = error

      if (response.status === 401) {
        try {
          const refreshRes = await axiosInstance.get('/members/refresh')
          if (refreshRes.status === 200) {
            const { authorization } = refreshRes.headers
            saveAccessTokenToLocalStorage(authorization)
            config.headers.Authorization = authorization
            return await axios(config)
          }
        } catch (error) {
          return Promise.reject(error)
        }
      }
      return Promise.reject(error)
    }
  )
  return instance
}

const axiosInstance = createAxiosInstance()
const fileAxios = createFileAxiosInstance()
const authInstance = createAuthAxiosInstance()

export { axiosInstance, fileAxios, authInstance }
