import { forwardRef } from 'react'
import { useGoogleMap } from '@ubilabs/google-maps-react-hooks'
import useMarker from '../../../hooks/useMarker'
import usePolyline from '../../../hooks/usePolyline'

type MapCanvasProps = {
  style: React.CSSProperties
  position: google.maps.LatLngLiteral | null
  path: google.maps.LatLngLiteral[] | null
}

const MapCanvas = forwardRef<HTMLDivElement, MapCanvasProps>((props, ref) => {
  const { style, position, path } = props
  const map = useGoogleMap() || null
  useMarker({ map, position })
  usePolyline({ map, path })

  return <div ref={ref} style={style} />
})

MapCanvas.displayName = 'MapCanvas'

export default MapCanvas
