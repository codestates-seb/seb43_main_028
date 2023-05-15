import { createBrowserRouter } from 'react-router-dom'
import { Suspense } from 'react'
import GeneralLayout from '../layout/GeneralLayout'
import { routerData } from './routerData'

const router = createBrowserRouter(
  routerData.map(routerElement => ({
    path: routerElement.path,
    element: (
      <GeneralLayout showTapBar={routerElement.showTapBar}>
        <Suspense fallback={<div>Loading...</div>}>{routerElement.element}</Suspense>
      </GeneralLayout>
    ),
  }))
)

export default router
