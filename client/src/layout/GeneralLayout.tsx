import { useState, useEffect } from 'react'
import { useAtom } from 'jotai'
import Tapbar from '../components/common/Tapbar'
import { idAtom, isLoginAtom, userAtom } from '../store/authAtom'
import useRouter from '../hooks/useRouter'
import { getCurrentUserInfo, refreshAccessToken } from '../apis/user'
import { getRefreshTokenFromLocalStorage } from '../utils/refreshTokenHandler'
import { TapBarContent } from '../router/routerData'

type GeneralLayoutProps = {
  children: React.ReactNode
  showTapBar: boolean
  withAuth: boolean
}

export default function GeneralLayout({ children, showTapBar, withAuth }: GeneralLayoutProps) {
  const [isAuthChecking, setIsAuthChecking] = useState(true)
  const [id, setId] = useAtom(idAtom)
  const [isLogin, setIsLogin] = useAtom(isLoginAtom)
  const [, setUser] = useAtom(userAtom)
  const { routeTo, pathname } = useRouter()

  useEffect(() => {
    const authHandler = async () => {
      const memberId = localStorage.getItem('loggedId')
      if (!memberId) {
        localStorage.removeItem('loggedId')
        setIsLogin(false)
        console.log('로그아웃')
        return 'fail'
      }

      const userInfoRes = await getCurrentUserInfo(+memberId)
      if (userInfoRes) {
        setIsLogin(true)
        setId(+memberId)
        setUser(userInfoRes)
        console.log('로그인 상태 유지중')
        return 'success'
      }

      const isRefreshed = await refreshAccessToken()
      if (isRefreshed === 'success') {
        const refreshedUserInfoRes = await getCurrentUserInfo(+memberId)
        if (refreshedUserInfoRes) {
          setIsLogin(true)
          setId(+memberId)
          setUser(refreshedUserInfoRes)
          console.log('로그인 상태 유지중')
          return 'success'
        }
      }

      localStorage.removeItem('loggedId')
      setIsLogin(false)
      console.log('로그아웃')
      return 'fail'
    }

    authHandler().then(() => {
      setIsAuthChecking(false)
      // if (isLogin && (pathname === '/signin' || pathname === '/signup')) routeTo('/')
      // if (!isLogin && withAuth) routeTo('/signin')
    })
  }, [pathname])

  return (
    <>
      {isAuthChecking || children}
      {showTapBar && <Tapbar tapBarContent={TapBarContent} />}
    </>
  )
}
