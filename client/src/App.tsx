import { useState, useCallback } from 'react'
import { RouterProvider } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { GoogleMapsProvider } from '@ubilabs/google-maps-react-hooks'
import MapRefContext from './contexts/mapRefContext'
import router from './router'
import './styles/global.scss'

type MapContainerType = HTMLDivElement | null

function App() {
  const queryClient = new QueryClient()
  const [mapContainer, setMapContainer] = useState<MapContainerType>(null)
  const mapRef = useCallback((node: React.SetStateAction<MapContainerType>) => {
    node && setMapContainer(node)
  }, [])

  return (
    <QueryClientProvider client={queryClient}>
      <GoogleMapsProvider
        googleMapsAPIKey={import.meta.env.VITE_GOOGLE_MAPS_API_KEY}
        mapContainer={mapContainer}
      >
        <MapRefContext.Provider value={mapRef}>
          <RouterProvider router={router} />
        </MapRefContext.Provider>
      </GoogleMapsProvider>
    </QueryClientProvider>
  )
}

export default App
