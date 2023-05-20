import { AxiosError } from 'axios'
import {
  saveRefreshTokenToLocalStorage,
  getRefreshTokenFromLocalStorage,
  removeRefreshTokenFromLocalStorage,
} from '../utils/refreshTokenHandler'
import { axiosInstance, fileAxios } from './instance'

type SignUpPropsType = {
  nickname: string
  email: string
  password: string
}

type SignInPropsType = {
  email: FormDataEntryValue | null
  password: FormDataEntryValue | null
  autoLogin: boolean
}

type SignInResType = {
  status: 'success' | 'fail'
  memberId: number | null
}

export type UserInfoType = {
  defaultWalkLogPublicSetting: string
  email: string
  imageUrl: string
  introduction: string
  memberId: number
  nickname: string
  createdAt: string
  totalWalkLog: number
  totalWalkLogContent: number
}

// GET 요청만 되고,
// POST, PATCH, DELETE 요청은 403 forbidden (Invalid CORS Request)
// 백엔드 서버에서 프론트 배포 서버만 허용해줬던 origin을 *(모두)로 변경

export const signUp = async ({ nickname, email, password }: SignUpPropsType) => {
  try {
    await axiosInstance.post('/api/members/sign', { nickname, email, password })
    return 'success'
  } catch (error: unknown) {
    console.log(error)
    return (error as AxiosError)?.response?.status === 409 ? '409-fail' : 'fail'
  }
}

export const signIn = async ({
  email,
  password,
  autoLogin = true,
}: SignInPropsType): Promise<SignInResType> => {
  try {
    const response = await axiosInstance.post('/api/members/login', { email, password, autoLogin })
    const { authorization } = response.headers
    axiosInstance.defaults.headers.common.Authorization = authorization
    saveRefreshTokenToLocalStorage(response.headers.refresh)
    return { status: 'success', memberId: response.data.memberId }
  } catch (error) {
    return { status: 'fail', memberId: null }
  }
}

export const getCurrentUserInfo = async (url: string): Promise<UserInfoType> => {
  try {
    const response = await axiosInstance.get(url)
    return response.data
  } catch (error) {
    throw new Error('Failed to fetch user info')
  }
}

export const refreshAccessToken = async () => {
  try {
    const { headers } = await axiosInstance.get('/members/refresh', {
      headers: {
        Refresh: getRefreshTokenFromLocalStorage(),
      },
    })
    axiosInstance.defaults.headers.common.Authorization = headers.authorization
    return 'success'
  } catch (error) {
    removeRefreshTokenFromLocalStorage()
    return 'fail'
  }
}

export const patchUserProfile = async (url: string, formData: FormData) => {
  try {
    const response = await fileAxios.patch(url, formData)
    return response.data
  } catch (error: unknown) {
    console.log(error)
    return 'fail'
  }
}

export const patchUserPrivacySettings = async (url: string, data: any) => {
  try {
    const response = await fileAxios.patch(url, data)
    return response.data
  } catch (error: unknown) {
    console.log(error)
    return 'fail'
  }
}

export const patchUserPassword = async (url: string, passwordData: any) => {
  try {
    await axiosInstance.patch(url, passwordData)
    return 'success'
  } catch (error: unknown) {
    console.log(error)
    return 'fail'
  }
}

export const getUserTempPassword = async (url: string, email: any) => {
  try {
    await axiosInstance.post(url, email)
    return 'success'
  } catch (error: unknown) {
    console.log(error)
    return 'fail'
  }
}

export const unregisterUser = async (url: string) => {
  try {
    await axiosInstance.delete(url)
    return 'success'
  } catch (error) {
    console.log(error)
    return 'fail'
  }
}
