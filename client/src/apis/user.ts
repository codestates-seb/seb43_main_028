import axios, { AxiosError } from 'axios'

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

// GET 요청만 되고,
// POST, PATCH, DELETE 요청은 403 forbidden (Invalid CORS Request)

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

    return 'success'
  } catch (error) {
    return 'fail'
  }
}

export const getCurrentUserInfo = async () => {
  axios('/api/members/2', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'ngrok-skip-browser-warning': '69420',
    },
  })
    .then(response => {
      console.log(response.data)
      return response.data
    })
    .catch(err => 'fail')
}
