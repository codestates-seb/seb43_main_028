import { forwardRef, useState } from 'react'
import { useGoogleMap } from '@ubilabs/google-maps-react-hooks'
import useMarker from '../../../hooks/useMarker'
import usePolyline from '../../../hooks/usePolyline'
import { MapButton } from './MapButton'
import styles from './MapCanvas.module.scss'

export enum MapStyleType {
  HOME = 'HOME',
  WALK = 'WALK',
  HISTORY = 'HISTORY',
  FULL = 'FULL',
}

type MapCanvasProps = {
  styleType: MapStyleType
  position?: google.maps.LatLngLiteral | null
  path?: google.maps.LatLngLiteral[] | null
}

const MapCanvas = forwardRef<HTMLDivElement, MapCanvasProps>(
  ({ styleType, position = null, path = null }, ref) => {
    const [isFullScreenMode, setIsFullScreenMode] = useState<boolean>(false)

    const map = useGoogleMap() || null
    useMarker({ map, position })
    usePolyline({ map, path })

    const toggleScreenSize = () => setIsFullScreenMode(prev => !prev)
    const panToCurrentPosition = () => position && map?.panTo(position)

    return (
      <div className={isFullScreenMode ? styles[MapStyleType.FULL] : styles[styleType]}>
        <div className={styles.mapRef} ref={ref} />
        <div className={styles.btnWrapper}>
          <MapButton name={isFullScreenMode ? 'reduce' : 'full'} handleClick={toggleScreenSize} />
          <MapButton name='gps' handleClick={panToCurrentPosition} />
        </div>
      </div>
    )
  }
)

MapCanvas.displayName = 'MapCanvas'

export default MapCanvas
