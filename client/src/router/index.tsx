import { createBrowserRouter } from 'react-router-dom'
import { Suspense } from 'react'
import GeneralLayout from '../layout/GeneralLayout'
import { routerData } from './routerData'

const router = createBrowserRouter(
  routerData.map(router => ({
    path: router.path,
    element: (
      <GeneralLayout>
        <Suspense fallback={<div>Loading...</div>}>{router.element}</Suspense>
      </GeneralLayout>
    ),
  }))
)

export default router
