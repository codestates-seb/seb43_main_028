import { forwardRef, useState } from 'react'
import { useGoogleMap } from '@ubilabs/google-maps-react-hooks'
import useMarker from '../../../hooks/useMarker'
import usePolyline from '../../../hooks/usePolyline'
import { MapButton } from './MapButton'
import styles from './Map.module.scss'

export enum MapSize {
  BASIC = 'basic',
  LARGE = 'large',
}

type LiveMapProps = {
  mapSize?: MapSize
  path?: google.maps.LatLngLiteral[] | null
}

const LiveMap = forwardRef<HTMLDivElement, LiveMapProps>(
  ({ mapSize = MapSize.BASIC, path = null }, ref) => {
    const [isFullScreenMode, setIsFullScreenMode] = useState<boolean>(false)

    const liveMarkerIcon = {
      path: 0,
      scale: 10,
      fillColor: '#8cff9e',
      fillOpacity: 1,
      strokeWeight: 4,
      strokeColor: 'white',
    }

    const position = path ? path[path.length - 1] : null

    const map = useGoogleMap() || null
    useMarker({ map, position, icon: liveMarkerIcon })
    usePolyline({ map, path })

    const toggleScreenSize = () => setIsFullScreenMode(prev => !prev)
    const panToCurrentPosition = () => position && map?.panTo(position)

    return (
      <div className={isFullScreenMode ? styles.fullScreen : styles[mapSize]}>
        <div className={styles.mapRef} ref={path ? ref : null} />
        <div className={styles.btnWrapper}>
          <MapButton name={isFullScreenMode ? 'reduce' : 'full'} handleClick={toggleScreenSize} />
          <MapButton name='gps' handleClick={panToCurrentPosition} />
        </div>
      </div>
    )
  }
)

LiveMap.displayName = 'LiveMap'

export default LiveMap
