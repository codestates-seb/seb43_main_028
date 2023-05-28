// import { useState, useEffect } from 'react'
// import { useAtom } from 'jotai'
import Tapbar from '../components/common/Tapbar'
// import { userInfoAtom } from '../store/authAtom'
// import useRouter from '../hooks/useRouter'
// import { getCurrentUserInfo } from '../apis/user'
import { TapBarContent } from '../router/routerData'
// import Spinner from '../pages/loadingPage/Spinner'

type GeneralLayoutProps = {
  children: React.ReactNode
  showTapBar: boolean
  withAuth: boolean
}

export default function GeneralLayout({ children, showTapBar, withAuth }: GeneralLayoutProps) {
  // const [isAuthChecking, setIsAuthChecking] = useState(true)
  // const [, setUserInfo] = useAtom(userInfoAtom)
  // const [isLogin, setIsLogin] = useAtom(isLoginAtom)
  // const [, setUser] = useAtom(userAtom)
  // const [, setId] = useAtom(idAtom)
  // const { routeTo, pathname } = useRouter()

  // const authHandler = async () => {
  //   const userInfo = await getCurrentUserInfo()
  //   console.log('authHandler res', userInfo)
  //   setUserInfo(userInfo)
  // }
  console.log('withAuth', withAuth)

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

  // if (isAuthChecking) return <Spinner />

  return (
    <>
      {children}
      {showTapBar && <Tapbar tapBarContent={TapBarContent} />}
    </>
  )
}
