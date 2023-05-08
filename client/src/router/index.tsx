import { createBrowserRouter } from 'react-router-dom'
import App from '../App'
import {
  Home,
  Feed,
  MyPage,
  AfterWalk,
  OnWalk,
  HistoryDetail,
  HistoryList,
  SignIn,
  SignUp,
  NotFound,
} from '../pages'

import Map from '../components/common/Map'

const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    errorElement: <NotFound />,
    children: [
      { index: true, element: <Home /> },
      { path: 'feed', element: <Feed /> },
      { path: 'mypage', element: <MyPage /> },
      { path: 'afterwalk', element: <AfterWalk /> },
      { path: 'onwalk', element: <OnWalk /> },
      { path: 'history/:id', element: <HistoryDetail /> },
      { path: 'history', element: <HistoryList /> },
      { path: 'signin', element: <SignIn /> },
      { path: 'signup', element: <SignUp /> },
      { path: 'maptest', element: <Map /> },
    ],
  },
])

export default router
