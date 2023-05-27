import { useState, useCallback, useEffect } from 'react'
import { RouterProvider } from 'react-router-dom'
import { GoogleMapsProvider } from '@ubilabs/google-maps-react-hooks'
import MapRefContext from './contexts/mapRefContext'
import router from './router'
import './styles/global.scss'
import Landing from './pages/Landing'

function App() {
  const [mapContainer, setMapContainer] = useState<HTMLDivElement | null>(null)
  const mapRef = useCallback((node: React.SetStateAction<HTMLDivElement | null>) => {
    node && setMapContainer(node)
  }, [])

  const [isInitialLoad, setIsInitialLoad] = useState(false)

  const handleInitialLoad = () => {
    setIsInitialLoad(false)
  }

  useEffect(() => {
    sessionStorage.setItem('initialLoad', JSON.stringify(true))
    const InitialLoadItem = sessionStorage.getItem('initialLoad')
    const isInitial = InitialLoadItem ? JSON.parse(InitialLoadItem) : false
    if (isInitial) {
      setIsInitialLoad(true)
    }
  }, [])

  return (
    <GoogleMapsProvider
      googleMapsAPIKey={import.meta.env.VITE_GOOGLE_MAPS_API_KEY}
      mapContainer={mapContainer}
      mapOptions={{
        zoom: 18,
        mapTypeControl: false,
        streetViewControl: false,
        gestureHandling: 'greedy',
        zoomControl: false,
        fullscreenControl: false,
        scaleControl: false,
        styles: [
          { featureType: 'all', elementType: 'labels', stylers: [{ visibility: 'off' }] },
          { featureType: 'road', elementType: 'geometry', stylers: [] },
        ],
      }}
    >
      <MapRefContext.Provider value={mapRef}>
        {isInitialLoad ? (
          <Landing onInitialLoad={handleInitialLoad} />
        ) : (
          <RouterProvider router={router} />
        )}
      </MapRefContext.Provider>
    </GoogleMapsProvider>
  )
}

export default App
