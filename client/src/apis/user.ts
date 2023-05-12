import axios, { AxiosError } from 'axios'
import {
  saveRefreshTokenToLocalStorage,
  getRefreshTokenFromLocalStorage,
  removeRefreshTokenFromLocalStorage,
} from '../utils/refreshTokenHandler'

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

type UserInfoType = {
  defaultWalkLogPublicSetting: string
  email: string | null
  imageUrl: string | null
  introduction: string | null
  memberId: number
  nickname: string | null
  totalWalkLog: number
  totalWalkLogContent: number
}

// GET 요청만 되고,
// POST, PATCH, DELETE 요청은 403 forbidden (Invalid CORS Request)
// 백엔드 서버에서 프론트 배포 서버만 허용해줬던 origin을 *(모두)로 변경

export const signUp = async ({ nickname, email, password }: SignUpPropsType) => {
  try {
    await axios.post('/api/members/sign', { nickname, email, password })
    return 'success'
  } catch (error: unknown) {
    console.log(error)
    return (error as AxiosError)?.response?.status === 409 ? '409-fail' : 'fail'
  }
}

export const signIn = async ({ email, password, autoLogin = true }: SignInPropsType) => {
  try {
    const response = await axios.post('/api/members/login', { email, password, autoLogin })
    const { authorization } = response.headers
    axios.defaults.headers.common.Authorization = authorization
    saveRefreshTokenToLocalStorage(response.headers.refresh)
    return response.data.memberId
  } catch (error) {
    return 'fail'
  }
}

export const getCurrentUserInfo = async (url: string): Promise<UserInfoType> => {
  try {
    const response = await axios(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'ngrok-skip-browser-warning': '69420',
      },
    })
    return response.data
  } catch (error) {
    throw new Error('Failed to fetch user info')
  }
}

export const refreshAccessToken = async () => {
  try {
    const { headers } = await axios.get('/members/refresh', {
      headers: {
        Refresh: getRefreshTokenFromLocalStorage(),
      },
    })
    axios.defaults.headers.common.Authorization = headers.authorization
    return 'success'
  } catch (error) {
    removeRefreshTokenFromLocalStorage()
    return 'fail'
  }
}
