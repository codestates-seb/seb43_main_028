import { lazy } from 'react'

const Home = lazy(() => import('../pages/Home'))
const Feed = lazy(() => import('../pages/Feed'))
const MyPage = lazy(() => import('../pages/MyPage'))
const HistoryList = lazy(() => import('../pages/HistoryList'))
const HistoryDetail = lazy(() => import('../pages/HistoryDetail'))
const OnWalk = lazy(() => import('../pages/OnWalk'))
const AfterWalk = lazy(() => import('../pages/AfterWalk'))
const SignIn = lazy(() => import('../pages/SignIn'))
const SignUp = lazy(() => import('../pages/SignUp'))
const NotFound = lazy(() => import('../pages/NotFound'))

type RouterElement = {
  id: number
  path: string
  label: string
  element: React.ReactNode
  onTapBar: boolean
  showTapBar: boolean
  withAuth: boolean
}

export type TapBarElementType = {
  id: number
  path: string
  label: string
}

export const routerData: RouterElement[] = [
  {
    id: 0,
    path: '/',
    label: '홈',
    element: <Home />,
    onTapBar: true,
    showTapBar: true,
    withAuth: false,
  },
  {
    id: 3,
    path: '/history',
    label: '기록',
    element: <HistoryList />,
    onTapBar: true,
    showTapBar: true,
    withAuth: true,
  },
  {
    id: 4,
    path: '/history/:id',
    label: '기록상세',
    element: <HistoryDetail />,
    onTapBar: false,
    showTapBar: true,
    withAuth: true,
  },
  {
    id: 1,
    path: '/feed',
    label: '피드',
    element: <Feed />,
    onTapBar: true,
    showTapBar: true,
    withAuth: false,
  },
  {
    id: 2,
    path: '/mypage',
    label: '내정보',
    element: <MyPage />,
    onTapBar: true,
    showTapBar: true,
    withAuth: true,
  },
  {
    id: 5,
    path: '/onwalk',
    label: '걷기중',
    element: <OnWalk />,
    onTapBar: false,
    showTapBar: false,
    withAuth: false,
  },
  {
    id: 6,
    path: '/afterwalk',
    label: '걷기완료',
    element: <AfterWalk />,
    onTapBar: false,
    showTapBar: false,
    withAuth: false,
  },
  {
    id: 7,
    path: '/signin',
    label: '로그인',
    element: <SignIn />,
    onTapBar: false,
    showTapBar: true,
    withAuth: false,
  },
  {
    id: 8,
    path: '/signup',
    label: '회원가입',
    element: <SignUp />,
    onTapBar: false,
    showTapBar: true,
    withAuth: false,
  },
  {
    id: 9,
    path: '*',
    label: '404',
    element: <NotFound />,
    onTapBar: false,
    showTapBar: true,
    withAuth: false,
  },
]

export const TapBarContent: TapBarElementType[] = routerData.reduce((prev, router) => {
  if (!router.onTapBar) return prev
  return [
    ...prev,
    {
      id: router.id,
      path: router.path,
      label: router.label,
    },
  ]
}, [] as TapBarElementType[])
