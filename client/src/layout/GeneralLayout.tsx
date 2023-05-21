import { useState, useEffect } from 'react'
import { useAtom } from 'jotai'
import Tapbar from '../components/common/Tapbar'
import { idAtom, isLoginAtom, userAtom } from '../store/authAtom'
import useRouter from '../hooks/useRouter'
import { getUserInfo, refreshAccessToken } from '../apis/user'
import { TapBarContent } from '../router/routerData'

type GeneralLayoutProps = {
  children: React.ReactNode
  showTapBar: boolean
  withAuth: boolean
}

export default function GeneralLayout({ children, showTapBar, withAuth }: GeneralLayoutProps) {
  const [isAuthChecking, setIsAuthChecking] = useState(true)
  const [isLogin, setIsLogin] = useAtom(isLoginAtom)
  const [, setUser] = useAtom(userAtom)
  const [id, setId] = useAtom(idAtom)
  const { routeTo, pathname } = useRouter()

  useEffect(() => {
    const authHandler = async () => {
      const { userInfo } = await getUserInfo()
      if (userInfo === null) {
        console.log('userInfo 요청 실패')
        const isRefreshed = await refreshAccessToken()
        if (isRefreshed === 'success') {
          console.log('refresh 요청')
          const { userInfo } = await getUserInfo()
          if (userInfo) {
            setIsLogin(true)
            setId(userInfo.memberId)
            setUser(userInfo)
            console.log('로그인 상태 유지중')
            return 'login'
          }
        } else {
          setIsLogin(false)
          console.log('로그아웃')
          return 'logout'
        }
      } else {
        setIsLogin(true)
        setId(userInfo.memberId)
        setUser(userInfo)
        console.log('로그인 상태 유지중')
        return 'login'
      }
    }
    authHandler().then(() => {
      setIsAuthChecking(false)
      if (!isLogin && withAuth) routeTo('/signin')
      if (isLogin && (pathname === '/signin' || pathname === '/signup')) routeTo('/')
    })
  }, [pathname])

  return (
    <>
      {isAuthChecking || children}
      {showTapBar && <Tapbar tapBarContent={TapBarContent} />}
    </>
  )
}
