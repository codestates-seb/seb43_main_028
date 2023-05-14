import { useState, useEffect } from 'react'
import { Outlet } from 'react-router-dom'
import { useAtom } from 'jotai'
import { idAtom, isLoginAtom } from './store/authAtom'
import useRouter from './hooks/useRouter'
import { getCurrentUserInfo, refreshAccessToken } from './apis/user'
import { getRefreshTokenFromLocalStorage } from './utils/refreshTokenHandler'
import './styles/global.scss'

function App() {
  const [isAuthChecking, setIsAuthChecking] = useState(true)
  const [id, setId] = useAtom(idAtom)
  const [isLogin] = useAtom(isLoginAtom)
  const { currentPath } = useRouter()

  useEffect(() => {
    const authHandler = async () => {
      if (id === 0) {
        console.log('로그인하지 않은 상태입니다.')
        return 'fail'
      }
      const userInfoRes = await getCurrentUserInfo(`/api/members/${id}`)
      if (userInfoRes) {
        console.log('로그인 상태 유지중')
        return 'success'
      }

      if (!getRefreshTokenFromLocalStorage()) {
        console.log('refresh token 만료. 재로그인이 필요합니다.')
        return 'fail'
      }

      const refreshRes = await refreshAccessToken()
      if (refreshRes === 'success') {
        const newUserInfoRes = await getCurrentUserInfo(`/api/members/${id}`)
        if (newUserInfoRes && isLogin) {
          console.log('refresh token을 이용해 access token 갱신')
          return 'success'
        }
      }
      console.log('access token 갱신 실패')
      return 'fail'
    }

    authHandler().then(() => {
      setIsAuthChecking(false)
    })
  }, [currentPath, id, isLogin])

  return <Outlet />
}

export default App
