import { forwardRef } from 'react'
import { useGoogleMap } from '@ubilabs/google-maps-react-hooks'
import useMarker from '../../../hooks/useMarker'

type MapCanvasProps = {
  style: React.CSSProperties
  position: google.maps.LatLngLiteral | null
}

const MapCanvas = forwardRef<HTMLDivElement, MapCanvasProps>((props, ref) => {
  const { style, position, path } = props
  const map = useGoogleMap() || null
  useMarker({ map, position })

  return <div ref={ref} style={style} />
})

MapCanvas.displayName = 'MapCanvas'

export default MapCanvas
