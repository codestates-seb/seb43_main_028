import { useState, useEffect } from 'react'
import { useSetAtom } from 'jotai'
import useRouter from '../hooks/useRouter'
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
  const { pathname, routeTo } = useRouter()

  const [isAuthChecking, setIsAuthChecking] = useState(true)
  const setUserInfo = useSetAtom(userInfoAtom)

  const authHandler = async () => {
    const userInfo = await getCurrentUserInfo()
    setUserInfo(userInfo)

    if (!userInfo && withAuth) {
      routeTo('/signin')
    } else if (userInfo && (pathname === '/signin' || pathname === '/signup')) {
      routeTo('/')
    } else {
      setIsAuthChecking(false)
    }
  }

  useEffect(() => {
    authHandler()
  }, [pathname])

  if (isAuthChecking) return <Spinner />

  return (
    <>
      {children}
      {showTapBar && <Tapbar tapBarContent={TapBarContent} />}
    </>
  )
}
