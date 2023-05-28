/* eslint-disable no-param-reassign */
import axios from 'axios'
import {
  getAccessTokenFromLocalStorage,
  saveAccessTokenToLocalStorage,
} from '../utils/accessTokenHandler'

const axiosInstance = createAxiosInstance()
const fileAxios = createFileAxiosInstance()
const authInstance = createAuthAxiosInstance()

function createAxiosInstance() {
  const instance = axios.create({
    baseURL: import.meta.env.VITE_API_ENDPOINT,
    timeout: 3000,
    headers: {
      'Content-Type': 'application/json',
    },
    withCredentials: true,
  })

  instance.interceptors.request.use(config => {
    const accessToken = getAccessTokenFromLocalStorage()
    console.log('그냥인스턴스')
    config.headers.Authorization = accessToken || ''
    return config
  })

  return instance
}

function createFileAxiosInstance() {
  const instance = axios.create({
    baseURL: import.meta.env.VITE_API_ENDPOINT,
    timeout: 3000,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
    withCredentials: true,
  })

  instance.interceptors.request.use(config => {
    const accessToken = getAccessTokenFromLocalStorage()
    config.headers.Authorization = accessToken || ''
    return config
  })

  return instance
}

function createAuthAxiosInstance() {
  const instance = axios.create({
    baseURL: import.meta.env.VITE_API_ENDPOINT,
    timeout: 3000,
    headers: {
      'Content-Type': 'application/json',
    },
    withCredentials: true,
  })

  instance.interceptors.request.use(config => {
    const accessToken = getAccessTokenFromLocalStorage()
    config.headers.Authorization = accessToken || ''
    return config
  })

  instance.interceptors.response.use(
    response => response,
    async error => {
      const {
        config,
        response: { status },
      } = error

      if (status === 401) {
        try {
          const refreshRes = await axiosInstance.get('/members/refresh')
          if (refreshRes.status === 200) {
            saveAccessTokenToLocalStorage(refreshRes.headers.authorization)
            config.headers.Authorization = refreshRes.headers.authorization
            return await axios(config)
          }
        } catch (error) {
          console.log('refresh token error')
        }
      }
      return Promise.reject(error)
    }
  )

  return instance
}

export { axiosInstance, fileAxios, authInstance }
