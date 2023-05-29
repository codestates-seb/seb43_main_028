import { AxiosError } from 'axios'
import { authInstance, axiosInstance, fileAxios } from './instance'
import {
  removeAccessTokenFromLocalStorage,
  saveAccessTokenToLocalStorage,
} from '../utils/accessTokenHandler'
import { UserInfoAtomType } from '../store/authAtom'

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
  status: 'success' | 'fail' | 'invalid-info' | 'unknown-error'
  memberId: number | null
}

export const getCurrentUserInfo = async (): Promise<UserInfoAtomType | null> => {
  try {
    const response = await authInstance.get('/members/profile')
    return response.data
  } catch (error) {
    return null
  }
}

export const signUp = async ({ nickname, email, password }: SignUpPropsType) => {
  try {
    await axiosInstance.post('/members/sign', { nickname, email, password })
    return 'success'
  } catch (error: unknown) {
    const axiosError = error as AxiosError

    if (axiosError.response?.data) {
      const responseData = axiosError.response.data

      if (typeof responseData === 'object') {
        if ('message' in responseData) {
          if (responseData.message === 'Member Email exists') {
            return 'email-exists'
          }
          if (responseData.message === 'Member Nickname exists') {
            return 'nickname-exists'
          }
        }
      }
    }
    return 'unknown-error'
  }
}

export const signIn = async ({
  email,
  password,
  autoLogin = true,
}: SignInPropsType): Promise<SignInResType> => {
  try {
    const response = await axiosInstance.post('/members/login', { email, password, autoLogin })
    const { authorization } = response.headers
    saveAccessTokenToLocalStorage(authorization)
    return { status: 'success', memberId: response.data.memberId }
  } catch (error: unknown) {
    const axiosError = error as AxiosError

    if (axiosError.response?.data) {
      const responseData = axiosError.response.data

      if (typeof responseData === 'object') {
        if ('status' in responseData) {
          if (responseData.status === 401) {
            return { status: 'invalid-info', memberId: null }
          }
        }
      }
    }
    return { status: 'unknown-error', memberId: null }
  }
}

export const patchUserProfile = async (memberId: number, formData: FormData) => {
  try {
    const response = await fileAxios.patch(`/members/${memberId}`, formData)
    return { status: 'success', resData: response.data }
  } catch (error: unknown) {
    const axiosError = error as AxiosError
    if (axiosError.response?.data) {
      const responseData = axiosError.response.data
      if (typeof responseData === 'object') {
        if ('status' in responseData) {
          if (responseData.status === 409) {
            return { status: 'nickname-exists', resData: null }
          }
        }
      }
    }
  }
  return { status: 'unknown-error', resData: null }
}

export const patchUserPrivacySettings = async (memberId: number, data: any) => {
  try {
    const response = await fileAxios.patch(`/members/${memberId}`, data)
    return { status: 'success', resData: response.data }
  } catch (error: unknown) {
    console.log(error)
    return { status: 'fail', resData: null }
  }
}

export const patchUserPassword = async (memberId: number, passwordData: any) => {
  try {
    await axiosInstance.patch(`/members/${memberId}/pw`, passwordData)
    return 'success'
  } catch (error: unknown) {
    console.log(error)
    return 'fail'
  }
}

export const getUserTempPassword = async (email: any) => {
  try {
    await axiosInstance.post('/members/tmp-pw', email)
    return 'success'
  } catch (error: unknown) {
    console.log(error)
    return 'fail'
  }
}

export const logoutUser = async () => {
  try {
    await axiosInstance.post(`/members/logout`)
    removeAccessTokenFromLocalStorage()
    return 'success'
  } catch (error) {
    console.log(error)
    return 'fail'
  }
}

export const unregisterUser = async (memberId: number) => {
  try {
    await axiosInstance.delete(`/members/${memberId}`)
    removeAccessTokenFromLocalStorage()
    return 'success'
  } catch (error) {
    console.log(error)
    return 'fail'
  }
}
