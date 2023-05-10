import { GoogleMap, LoadScript, Marker, Polyline } from '@react-google-maps/api'
import { useState, useEffect } from 'react'
import styles from './Map.module.scss'

// 좌표 테스트 ing

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
  console.log('currentLocation: ', currentLocation)

  useEffect(() => {
    setIsLoadMap(true)
    setApiKey(import.meta.env.VITE_GOOGLE_MAPS_API_KEY || '')
    // navigator.geolocation.getCurrentPosition(
    //   position => {
    //     if (position) {
    //       const location = {
    //         lat: position.coords.latitude,
    //         lng: position.coords.longitude,
    //       }
    //       setCurrentLocation(location)
    //     }
    //   },
    //   function (error) {
    //     alert(`Error occurred. Error code: ${error.code}`)
    //   },
    //   { timeout: 5000 }
    // )
    return () => {
      setIsLoadMap(false)
    }
  }, [])

  function getDistance(lat1: number, lng1: number, lat2: number, lng2: number): number {
    console.log('getDistance 실행')
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
    const d = R * c * 1000 // Distance in m
    return d
  }

  function watchLocation() {
    let prevLocation: LatLng | undefined // 이전 위치

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
          console.log('location: ', location)
          // 현재 위치 갱신
          setCurrentLocation(() => location) // state
          // 직전 좌표가 없으면 현재 watchPosition으로 받은 location을 locations 배열에 추가
          if (!prevLocation) {
            setLocations([location])
            prevLocation = location
            console.log('prevLocation: ', prevLocation)
          }
          // 직전 좌표가 있으면 locations 배열에 현재 위치 추가
          console.log('location: ', location, 'prevLocation: ', prevLocation)
          if (location && prevLocation) {
            console.log('if문 안으로 들어옴')
            // 직전 좌표가 존재하면 현재 좌표와 직전 좌표의 거리를 계산
            const distanceFromLastPosition = getDistance(
              location.lat,
              location.lng,
              prevLocation.lat,
              prevLocation.lng
            )

            console.log('distanceFromLastPosition: ', distanceFromLastPosition)
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
              gestureHandling: 'greedy', // 한 손 가락으로 지도 핸들링
            }}
          >
            {currentLocation && <Marker position={currentLocation} />}
            {locations.length > 1 && (
              <Polyline
                path={locations}
                options={{
                  strokeColor: '#55de50',
                  strokeOpacity: 1,
                  strokeWeight: 6,
                }}
              />
            )}
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
