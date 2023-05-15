import { GoogleMap, LoadScript, Marker, Polyline } from '@react-google-maps/api'
import { useState, useEffect } from 'react'
import styles from './Map.module.scss'
import getDistance from '../../utils/calculate'
import { googleMapOptions, polylineOptions } from '../../utils/options'

// 좌표 테스트 ing

const containerStyle = {
  width: '340px',
  height: '340px',
}

type LatLngType = { lat: number; lng: number }

// test
function Map() {
  const [apiKey, setApiKey] = useState('')
  const [currentLocation, setCurrentLocation] = useState<LatLngType | undefined>(undefined)
  const [locations, setLocations] = useState<LatLngType[]>([])
  const [isLoadMap, setIsLoadMap] = useState(false)
  const [distance, setDistance] = useState(0)

  useEffect(() => {
    setApiKey(import.meta.env.VITE_GOOGLE_MAPS_API_KEY || '')
    navigator.geolocation.getCurrentPosition(
      position => {
        if (position) {
          const location = {
            lat: position.coords.latitude,
            lng: position.coords.longitude,
          }
          setCurrentLocation(location)
        }
      },
      function (error) {
        alert(`Error occurred. Error code: ${error.code}`)
      },
      { timeout: 5000 }
    )
    setIsLoadMap(true)
    return () => {
      setIsLoadMap(false)
    }
  }, [])

  function watchLocation() {
    let prevLocation: LatLngType | undefined // 이전 위치

    if (navigator.geolocation) {
      // 기기의 현재 위치를 탐색하는 브라우저 api 사용
      navigator.geolocation.watchPosition(
        position => {
          console.log('watchPosition 실행')
          // 좌표를 담아둘 객체 정의
          const location = {
            lat: position.coords.latitude,
            lng: position.coords.longitude,
          }
          // 현재 위치 갱신
          setCurrentLocation(() => location) // state
          // 직전 좌표가 없으면 현재 watchPosition으로 받은 location을 locations 배열에 추가
          if (!prevLocation) {
            setLocations([location])
            prevLocation = location
          }
          // 직전 좌표가 있으면 locations 배열에 현재 위치 추가
          // currentLocation에서 location으로 변경
          if (location && prevLocation) {
            // 직전 좌표가 존재하면 현재 좌표와 직전 좌표의 거리를 계산
            const distanceFromLastPosition = getDistance(
              location.lat,
              location.lng,
              prevLocation.lat,
              prevLocation.lng
            )

            // distance 상태를 갱신
            setDistance(distanceFromLastPosition)
            if (distanceFromLastPosition >= 5) {
              setLocations(prevLocations => [...prevLocations, location])
              prevLocation = location
              setDistance(0)
            }
          }
        },
        err => {
          console.log('watchPosition Error :', err)
        },
        {
          enableHighAccuracy: true,
          maximumAge: 0,
          timeout: 5000, // 5초마다 위치 정보 갱신 시도
        }
      )
    } else {
      console.log('Geolocation is not supported')
    }
  }

  return (
    <>
      {isLoadMap ? (
        <LoadScript googleMapsApiKey={apiKey} onLoad={() => console.log('Loaded!')}>
          <GoogleMap
            mapContainerStyle={containerStyle}
            zoom={14}
            center={currentLocation}
            options={googleMapOptions}
          >
            {currentLocation && <Marker position={currentLocation} />}
            {locations.length > 1 && <Polyline path={locations} options={polylineOptions} />}
          </GoogleMap>
        </LoadScript>
      ) : null}
      <div className={styles.btnBox}>
        <button type='button' className={styles.btn} onClick={watchLocation}>
          watchPosition
        </button>
      </div>
      <div className={styles.distanceBox}>
        <div>현재 좌표: </div>
        <div>
          {currentLocation
            ? `Lat: ${currentLocation.lat.toFixed(6)}, Lng: ${currentLocation.lng.toFixed(6)}`
            : '-'}
        </div>
      </div>
      <div className={styles.distanceBox}>
        <div>직전 좌표와의 거리: </div>
        <div>{distance.toFixed(8)}</div>
        <div>m</div>
      </div>
      <div className={styles.locationInfoBox}>
        <div>좌표 리스트</div>
        {locations.map(location => {
          return (
            <p key={Math.random()}>{`${location.lat.toFixed(4)}, ${location.lng.toFixed(4)}`}</p>
          )
        })}
      </div>
    </>
  )
}

export default Map
