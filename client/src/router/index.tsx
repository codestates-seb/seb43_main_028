import { createBrowserRouter } from 'react-router-dom'
import GeneralLayout from '../layout/GeneralLayout'
import { routerData } from './routerData'

const router = createBrowserRouter(
  routerData.map(routerElement => ({
    path: routerElement.path,
    element: (
      <GeneralLayout
        showTapBar={routerElement.showTapBar}
        withAuth={routerElement.withAuth}
        needInfo={routerElement.needInfo}
      >
        {routerElement.element}
      </GeneralLayout>
    ),
  }))
)

export default router
