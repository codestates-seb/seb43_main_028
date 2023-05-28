import { useState, useEffect } from 'react'
import { useSetAtom } from 'jotai'
// import useRouter from '../hooks/useRouter'
import { getCurrentUserInfo } from '../apis/user'
import { userInfoAtom } from '../store/authAtom'
import { TapBarContent } from '../router/routerData'
import Tapbar from '../components/common/Tapbar'
import Spinner from '../pages/loadingPage/Spinner'

type GeneralLayoutProps = {
  children: React.ReactNode
  showTapBar: boolean
  withAuth: boolean
}

export default function GeneralLayout({ children, showTapBar, withAuth }: GeneralLayoutProps) {
  console.log('withAuth', withAuth)

  const [isAuthChecking, setIsAuthChecking] = useState(true)
  const setUserInfo = useSetAtom(userInfoAtom)
  // const { routeTo, pathname } = useRouter()

  const authHandler = async () => {
    const userInfo = await getCurrentUserInfo()
    setUserInfo(userInfo)
    setIsAuthChecking(false)
  }

  useEffect(() => {
    console.log('app useEffect 실행')
    authHandler()
  }, [])
  // useEffect(() => {
  // console.log('app useEffect 실행')
  // authHandler().then(() => {
  //   setIsAuthChecking(false)
  // })
  // const authHandler = async () => {
  //   const { userInfo } = await getUserInfo()
  //   if (userInfo === null) {
  //     const isRefreshed = await refreshAccessToken()
  //     if (isRefreshed === 'success') {
  //       const { userInfo } = await getUserInfo()
  //       if (userInfo) {
  //         setIsLogin(true)
  //         setId(userInfo.memberId)
  //         setUser(userInfo)
  //         return 'login'
  //       }
  //     } else {
  //       setIsLogin(false)
  //       return 'logout'
  //     }
  //   } else {
  //     setIsLogin(true)
  //     setId(userInfo.memberId)
  //     setUser(userInfo)
  //     return 'login'
  //   }
  // }
  // authHandler().then(loginStatus => {
  //   setIsAuthChecking(false)
  //   if (!isLogin && withAuth && loginStatus === 'logout') routeTo('/signin')
  //   if (isLogin && (pathname === '/signin' || pathname === '/signup')) routeTo('/')
  // })
  // }, [])

  if (isAuthChecking) return <Spinner />

  return (
    <>
      {children}
      {showTapBar && <Tapbar tapBarContent={TapBarContent} />}
    </>
  )
}
