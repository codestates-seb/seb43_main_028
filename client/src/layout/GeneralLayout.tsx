import { useState, useEffect } from 'react'
import { useAtom } from 'jotai'
import Tapbar from '../components/common/Tapbar'
import { idAtom, isLoginAtom, userAtom } from '../store/authAtom'
import useRouter from '../hooks/useRouter'
import { getUserInfo, refreshAccessToken } from '../apis/user'
import { TapBarContent } from '../router/routerData'
import Spinner from '../pages/loadingPage/Spinner'
import { axiosInstance } from '../apis/instance'

type GeneralLayoutProps = {
  children: React.ReactNode
  showTapBar: boolean
  withAuth: boolean
  needInfo: boolean
}

export default function GeneralLayout({
  children,
  showTapBar,
  withAuth,
  needInfo,
}: GeneralLayoutProps) {
  const [isAuthChecking, setIsAuthChecking] = useState(true)
  const [isLogin, setIsLogin] = useAtom(isLoginAtom)
  const [, setUser] = useAtom(userAtom)
  const [, setId] = useAtom(idAtom)
  const { routeTo, pathname } = useRouter()

  useEffect(() => {
    const accessToken = localStorage.getItem('accessToken')
    if (accessToken) {
      axiosInstance.defaults.headers.common.Authorization = JSON.parse(accessToken)
    }
    const authHandler = async () => {
      // signin, signup, feed 페이지에서는 auth api 요청 자체를 보내지 않아도 됨
      if (!needInfo) {
        return
      }

      // 어차피 로컬스토리지에 저장된 token으로 verifying을 해도 user 정보는 불러와야 함
      // 아래 코드 두 줄을 activate 했을 때, 유저 정보 수정 시 새로고침 후에 반영이 안되는 이슈가 발생함

      // const isVerifiedUser = await accessTokenVerification()
      // if (isVerifiedUser) return

      const { userInfo } = await getUserInfo()
      if (userInfo === null) {
        const isRefreshed = await refreshAccessToken()
        if (isRefreshed === 'success') {
          const { userInfo } = await getUserInfo()
          if (userInfo) {
            setIsLogin(true)
            setId(userInfo.memberId)
            setUser(userInfo)
            return 'login'
          }
        } else {
          setIsLogin(false)
          return 'logout'
        }
      } else {
        setIsLogin(true)
        setId(userInfo.memberId)
        setUser(userInfo)
        return 'login'
      }
    }
    authHandler().then(loginStatus => {
      setIsAuthChecking(false)
      if (!isLogin && withAuth && loginStatus === 'logout') routeTo('/signin')
      if (isLogin && (pathname === '/signin' || pathname === '/signup')) routeTo('/')
    })
  }, [pathname, isLogin, withAuth])

  return (
    <>
      {isAuthChecking ? <Spinner /> : children}
      {showTapBar && <Tapbar tapBarContent={TapBarContent} />}
    </>
  )
}
