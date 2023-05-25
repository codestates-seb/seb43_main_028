import { forwardRef, useState } from 'react'
import { useGoogleMap } from '@ubilabs/google-maps-react-hooks'
import useMarker from '../../../hooks/useMarker'
import usePolyline from '../../../hooks/usePolyline'
import { MapButton } from './MapButton'
import styles from './Map.module.scss'
import { MapSize } from './LiveMap'

const stopMarkerIcon = {
  path: google.maps.SymbolPath.CIRCLE,
  scale: 8,
  fillColor: 'white',
  fillOpacity: 1,
  strokeWeight: 1,
  strokeColor: '#212121',
}

type StaticPathMapProps = {
  mapSize?: MapSize
  path?: google.maps.LatLngLiteral[] | null
}

const StaticPathMap = forwardRef<HTMLDivElement, StaticPathMapProps>(
  ({ mapSize = MapSize.BASIC, path = null }, ref) => {
    const [isFullScreenMode, setIsFullScreenMode] = useState<boolean>(false)

    const startAt = path ? path[0] : null
    const endAt = path ? path[path.length - 1] : null

    const map = useGoogleMap() || null
    useMarker({ map, position: startAt, icon: stopMarkerIcon })
    useMarker({ map, position: endAt, icon: stopMarkerIcon })
    usePolyline({ map, path })

    const toggleScreenSize = () => setIsFullScreenMode(prev => !prev)

    return (
      <div className={isFullScreenMode ? styles.fullScreen : styles[mapSize]}>
        <div className={styles.mapRef} ref={ref} />
        <div className={styles.btnWrapper}>
          <MapButton name={isFullScreenMode ? 'reduce' : 'full'} handleClick={toggleScreenSize} />
        </div>
      </div>
    )
  }
)

StaticPathMap.displayName = 'StaticPathMap'

export default StaticPathMap
