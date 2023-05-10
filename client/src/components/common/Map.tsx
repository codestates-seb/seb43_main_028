import { GoogleMap, LoadScript, Marker } from '@react-google-maps/api'
import { useState, useEffect } from 'react'
import styles from './Map.module.scss'

// 좌표 테스트

const containerStyle = {
  width: '340px',
  height: '340px',
}

type LatLng = { lat: number; lng: number }

// test
function Map() {
  const [apiKey, setApiKey] = useState('')
  const [currentLocation, setCurrentLocation] = useState<LatLng | undefined>(undefined)
  const [locations, setLocations] = useState<LatLng[]>([])
  const [isLoadMap, setIsLoadMap] = useState(false)
  const [distance, setDistance] = useState(0)

  function getDistance(lat1: number, lng1: number, lat2: number, lng2: number): number {
    function deg2rad(deg: number): number {
      return deg * (Math.PI / 180)
    }
    const R = 6371 // Radius of the earth in km
    const dLat = deg2rad(lat2 - lat1) // deg2rad below
    const dLon = deg2rad(lng2 - lng1)
    const a =
      Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2)
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    const d = (R * c) / 1000 // Distance in m
    return d
  }

  useEffect(() => {
    let prevLocation: LatLng | undefined // 이전 위치
    setIsLoadMap(true)
    if (navigator.geolocation) {
      console.log('geolocation 실행')
      // 기기의 현재 위치를 탐색하는 브라우저 api 사용
      navigator.geolocation.watchPosition(
        position => {
          // 좌표를 담아둘 객체 정의
          const location = {
            lat: position.coords.latitude,
            lng: position.coords.longitude,
          }
          // 현재 위치 갱신
          setCurrentLocation(location)
          // 직전 좌표가 없으면 현재 watchPosition으로 받은 location을 locations 배열에 추가
          if (!prevLocation) {
            setLocations([location])
            prevLocation = location
          }
          // 직전 좌표가 있으면 locations 배열에 현재 위치 추가
          if (currentLocation && prevLocation) {
            // 직전 좌표가 존재하면 현재 좌표와 직전 좌표의 거리를 계산
            const distanceFromLastPosition = getDistance(
              currentLocation.lat,
              currentLocation.lng,
              prevLocation.lat,
              prevLocation.lng
            )

            // distance 상태를 갱신
            setDistance(distanceFromLastPosition)
            if (distanceFromLastPosition >= 5) {
              setLocations(prevLocations => [...prevLocations, location])
              prevLocation = location
            }
          }
        },
        err => {
          console.log(err)
        },
        {
          enableHighAccuracy: false,
          maximumAge: 0,
          timeout: 5000, // 5초마다 위치 정보 갱신 시도
        }
      )
    } else {
      console.log('Geolocation is not supported')
    }
    // vercel에서 설정한 환경 변수를 가져옴
    setApiKey(import.meta.env.VITE_GOOGLE_MAPS_API_KEY || '')
    return () => {
      setIsLoadMap(false)
    }
  }, [currentLocation])

  return (
    <>
      {isLoadMap ? (
        <LoadScript googleMapsApiKey={apiKey} onLoad={() => console.log('Loaded!')}>
          <GoogleMap
            mapContainerStyle={containerStyle}
            zoom={14}
            center={currentLocation}
            options={{
              styles: [
                {
                  // 색상
                  featureType: 'all',
                  stylers: [
                    {
                      saturation: -100,
                    },
                  ],
                },
                {
                  // 물 색상
                  featureType: 'water',
                  stylers: [
                    {
                      color: '#7dcdcd',
                    },
                  ],
                },
                {
                  // 건물 이름 가리기
                  featureType: 'all',
                  elementType: 'labels',
                  stylers: [{ visibility: 'off' }],
                },

                {
                  // 지도 단순화하기
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
              mapTypeControl: false, // 지도 위성 끄기
              streetViewControl: false, // 사람 모양 끄기
            }}
          >
            {currentLocation && <Marker position={currentLocation} />}
          </GoogleMap>
        </LoadScript>
      ) : null}

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
        <div>{distance.toFixed(2)}</div>
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
