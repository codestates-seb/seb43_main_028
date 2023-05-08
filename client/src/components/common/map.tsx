import { GoogleMap, LoadScript } from '@react-google-maps/api'
import { useState, useEffect } from 'react'

const containerStyle = {
  width: '340px',
  height: '340px',
}

function Map() {
  const [apiKey, setApiKey] = useState('')

  useEffect(() => {
    const { VITE_GOOGLE_MAPS_API_KEY } = import.meta.env
    setApiKey(VITE_GOOGLE_MAPS_API_KEY)
  }, [])

  return (
    <LoadScript googleMapsApiKey={apiKey} onLoad={() => console.log('Loaded!')}>
      <GoogleMap
        mapContainerStyle={containerStyle}
        zoom={14}
        center={{ lat: 37.5662952, lng: 126.9779451 }}
      />
    </LoadScript>
  )
}

export default Map
