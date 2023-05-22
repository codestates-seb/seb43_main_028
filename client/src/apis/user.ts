import { AxiosError } from 'axios'
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
    await axiosInstance.post('/members/sign', { nickname, email, password })
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
    const response = await axiosInstance.post('/members/login', { email, password, autoLogin })
    const { authorization } = response.headers
    axiosInstance.defaults.headers.common.Authorization = authorization
    fileAxios.defaults.headers.common.Authorization = authorization
    return { status: 'success', memberId: response.data.memberId }
  } catch (error) {
    return { status: 'fail', memberId: null }
  }
}

export const getUserInfo = async () => {
  try {
    const response = await axiosInstance.get('/members/profile')
    return { status: 'success', userInfo: response.data }
  } catch (error) {
    console.error(error)
    return { status: 'fail', userInfo: null }
  }
}

export const getCurrentUserInfo = async (memberId: number): Promise<UserInfoType> => {
  try {
    const response = await axiosInstance.get(`/members/${memberId}`)
    return response.data
  } catch (error) {
    throw new Error('Failed to fetch user info')
  }
}

export const refreshAccessToken = async () => {
  try {
    const { headers } = await axiosInstance.get('/members/refresh')
    axiosInstance.defaults.headers.common.Authorization = headers.authorization
    return 'success'
  } catch (error) {
    return 'fail'
  }
}

export const patchUserProfile = async (memberId: number, formData: FormData) => {
  try {
    const response = await fileAxios.patch(`/members/${memberId}`, formData)
    return { status: 'success', resData: response.data }
  } catch (error: unknown) {
    console.log(error)
    return { status: 'fail', resData: null }
  }
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
    delete axiosInstance.defaults.headers.common.Authorization
    delete fileAxios.defaults.headers.common.Authorization
    return 'success'
  } catch (error) {
    console.log(error)
    return 'fail'
  }
}

export const unregisterUser = async (memberId: number) => {
  try {
    await axiosInstance.delete(`/members/${memberId}`)
    return 'success'
  } catch (error) {
    console.log(error)
    return 'fail'
  }
}
