import { forwardRef } from 'react'
import { useGoogleMap } from '@ubilabs/google-maps-react-hooks'
import useMarker from '../../../hooks/useMarker'
import usePolyline from '../../../hooks/usePolyline'

const SEOUL_POSITION = { lat: 37.5642135, lng: 127.0016985 }

const defaultMapOptions: google.maps.MapOptions = {
  zoom: 18,
  mapTypeControl: false,
  streetViewControl: false,
  gestureHandling: 'greedy',
  zoomControl: false,
  fullscreenControl: true,
  scaleControl: false,
  styles: [
    { featureType: 'all', elementType: 'labels', stylers: [{ visibility: 'off' }] },
    { featureType: 'road', elementType: 'geometry', stylers: [] },
  ],
}

type MapCanvasProps = {
  style: React.CSSProperties
  position: google.maps.LatLngLiteral | null
  path: google.maps.LatLngLiteral[] | null
}

const MapCanvas = forwardRef<HTMLDivElement, MapCanvasProps>((props, ref) => {
  const { style, position, path } = props

  const map = useGoogleMap() || null

  map?.setOptions(defaultMapOptions)
  map?.setCenter(position || SEOUL_POSITION)

  useMarker({ map, position })
  usePolyline({ map, path })

  return <div ref={ref} style={style} />
})

MapCanvas.displayName = 'MapCanvas'

export default MapCanvas
