import axios, { AxiosError } from 'axios'

type SignUpPropsType = {
  displayName: string
  email: string
  password: string
}

export const signUp = async ({ displayName, email, password }: SignUpPropsType) => {
  try {
    await axios.post('/members/sign', { displayName, email, password })
    return 'success'
  } catch (error: unknown) {
    if ((error as AxiosError)?.response?.status === 409) {
      return '409-fail'
    } else {
      return 'fail'
    }
  }
}
