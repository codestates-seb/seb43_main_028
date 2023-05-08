import { GoogleMap, LoadScript } from '@react-google-maps/api'
import { useState, useEffect } from 'react'

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

  useEffect(() => {
    let prevLocation: LatLng | undefined // 이전 위치
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
            setLocations(prevLocations => [...prevLocations, location])
            prevLocation = location
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
  }, [currentLocation, locations])

  useEffect(() => {
    const { VITE_GOOGLE_MAPS_API_KEY } = import.meta.env
    setApiKey(VITE_GOOGLE_MAPS_API_KEY)
  }, [])

  return (
    <>
      <LoadScript googleMapsApiKey={apiKey} onLoad={() => console.log('Loaded!')}>
        <GoogleMap mapContainerStyle={containerStyle} zoom={14} center={currentLocation} />
      </LoadScript>
      <ul>
        {locations.length >= 1
          ? locations.map(location => {
              return (
                <li key={Math.random()}>
                  <div>{location.lat.toFixed(4)}</div>
                  <div>{location.lng.toFixed(4)}</div>
                </li>
              )
            })
          : null}
      </ul>
    </>
  )
}

export default Map
