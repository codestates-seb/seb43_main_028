import axios, { AxiosError } from 'axios'

type SignUpPropsType = {
  displayName: string
  email: string
  password: string
}

type SignInPropsType = {
  email: FormDataEntryValue | null
  password: FormDataEntryValue | null
  autoLogin: boolean
}

export const signUp = async ({ displayName, email, password }: SignUpPropsType) => {
  try {
    await axios.post('/members/sign', { displayName, email, password })
    return 'success'
  } catch (error: unknown) {
    return (error as AxiosError)?.response?.status === 409 ? '409-fail' : 'fail'
  }
}

export const signIn = async ({ email, password, autoLogin = true }: SignInPropsType) => {
  try {
    const response = await axios.post('/members/login', { email, password, autoLogin })
    const { authorization } = response.headers

    axios.defaults.headers.common.Authorization = authorization

    return 'success'
  } catch (error) {
    return 'fail'
  }
}
