import { Suspense, lazy } from 'react'
import ChangePassword from '../pages/ChangePassword'
import FindPassword from '../pages/FindPassword'
import HistoryDetailLoading from '../pages/loadingPage/HistoryDetailLoading'
import HistoryListLoading from '../pages/loadingPage/HistoryListLoading'
import HomeLoading from '../pages/loadingPage/HomeLoading'
import FeedLoading from '../pages/loadingPage/FeedLoading'
import MypPageLoading from '../pages/loadingPage/MyPageLoading'
import OnWalkLoading from '../pages/loadingPage/OnWalkLoading'
import AfterWalkLoading from '../pages/loadingPage/AfterWalkLoading'

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
    id: 1,
    path: '/',
    label: '홈',
    element: (
      <Suspense fallback={<HomeLoading />}>
        <Home />
      </Suspense>
    ),
    onTapBar: true,
    showTapBar: true,
    withAuth: false,
  },
  {
    id: 2,
    path: '/history',
    label: '기록',
    element: (
      <Suspense fallback={<HistoryListLoading />}>
        <HistoryList />
      </Suspense>
    ),
    onTapBar: true,
    showTapBar: true,
    withAuth: true,
  },
  {
    id: 3,
    path: '/history/:id',
    label: '기록상세',
    element: (
      <Suspense fallback={<HistoryDetailLoading />}>
        <HistoryDetail />
      </Suspense>
    ),
    onTapBar: false,
    showTapBar: true,
    withAuth: true,
  },
  {
    id: 4,
    path: '/feed',
    label: '피드',
    element: (
      <Suspense fallback={<FeedLoading />}>
        <Feed />
      </Suspense>
    ),
    onTapBar: true,
    showTapBar: true,
    withAuth: false,
  },
  {
    id: 5,
    path: '/mypage',
    label: '내정보',
    element: (
      <Suspense fallback={<MypPageLoading />}>
        <MyPage />
      </Suspense>
    ),
    onTapBar: true,
    showTapBar: true,
    withAuth: true,
  },
  {
    id: 6,
    path: '/onwalk/:id',
    label: '걷기중',
    element: (
      <Suspense fallback={<OnWalkLoading />}>
        <OnWalk />
      </Suspense>
    ),
    onTapBar: false,
    showTapBar: false,
    withAuth: false,
  },
  {
    id: 7,
    path: '/afterwalk/:id',
    label: '걷기완료',
    element: (
      <Suspense fallback={<AfterWalkLoading />}>
        <AfterWalk />
      </Suspense>
    ),
    onTapBar: false,
    showTapBar: false,
    withAuth: false,
  },
  {
    id: 8,
    path: '/signin',
    label: '로그인',
    element: (
      <Suspense fallback=''>
        <SignIn />
      </Suspense>
    ),
    onTapBar: false,
    showTapBar: false,
    withAuth: false,
  },
  {
    id: 9,
    path: '/signup',
    label: '회원가입',
    element: (
      <Suspense fallback=''>
        <SignUp />
      </Suspense>
    ),
    onTapBar: false,
    showTapBar: false,
    withAuth: false,
  },
  {
    id: 10,
    path: '/changepassword',
    label: '비밀번호변경',
    element: (
      <Suspense fallback=''>
        <ChangePassword />
      </Suspense>
    ),
    onTapBar: false,
    showTapBar: false,
    withAuth: false,
  },
  {
    id: 11,
    path: '/findpassword',
    label: '비밀번호찾기',
    element: (
      <Suspense fallback=''>
        <FindPassword />
      </Suspense>
    ),
    onTapBar: false,
    showTapBar: false,
    withAuth: false,
  },
  {
    id: 12,
    path: '*',
    label: '404',
    element: (
      <Suspense fallback=''>
        <NotFound />
      </Suspense>
    ),
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
