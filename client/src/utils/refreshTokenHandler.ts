const isBrowser = () => typeof window !== 'undefined'

const saveRefreshTokenToLocalStorage = (refreshToken: string) => {
  if (isBrowser()) {
    try {
      localStorage.setItem('refreshToken', refreshToken)
    } catch (error) {
      console.error('Error saving refreshToken to local storage:', error)
    }
  }
}

export default saveRefreshTokenToLocalStorage
