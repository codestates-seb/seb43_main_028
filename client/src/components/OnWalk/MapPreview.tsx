import Icon from '../common/Icon'
import StaticMap from '../Map/StaticMap'
import styles from './MapPreview.module.scss'

const staticPreviewMapOptions = {
  zoom: 16,
  mapTypeControl: false,
  streetViewControl: false,
  gestureHandling: 'none',
  zoomControl: false,
  fullscreenControl: false,
  scaleControl: false,
  styles: [
    { featureType: 'all', stylers: [{ saturation: -100 }] },
    { featureType: 'water', stylers: [{ color: '#7dcdcd' }] },
    { featureType: 'all', elementType: 'labels', stylers: [{ visibility: 'off' }] },
    {
      featureType: 'road',
      elementType: 'geometry',
      stylers: [
        { visibility: 'simplified' },
        { hue: '#000000' },
        { saturation: -50 },
        { lightness: -15 },
        { weight: 1.5 },
      ],
    },
  ],
}

type MapPreviewProps = {
  size: { width: string; height: string; borderRadius: string }
  coordinates: google.maps.LatLngLiteral[]
}

export default function MapPreview({ size, coordinates }: MapPreviewProps) {
  const { width, height, borderRadius } = size

  return (
    <div className={styles.container}>
      <div className={styles.regionBox}>
        <div className={styles.currentRegion}>
          <Icon name='pin' /> 서울시 동대문구
        </div>
        <button className={styles.refreshButton} type='button'>
          10초 전
          <Icon name='refresh' />
        </button>
      </div>
      <StaticMap
        mapOptions={staticPreviewMapOptions}
        coordinates={coordinates}
        size={{ width, height, borderRadius }}
      />
      <button className={styles.openMapButton} type='button'>
        지금까지 걸은 경로 확인하기
        <Icon name='map-color' />
        <Icon name='arrow-right' />
      </button>
    </div>
  )
}
