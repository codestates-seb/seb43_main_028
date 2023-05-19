import { useState, useEffect } from 'react'
import { useAtomValue } from 'jotai'
import { userAtom, isLoginAtom } from '../store/authAtom'
import useMapRef from '../hooks/useMapRef'
import MapCanvas from '../components/common/Map/MapCanvas'
import HomeHeader from '../components/Home/HomeHeader'
import useRouter from '../hooks/useRouter'
import styles from './Home.module.scss'

export default function Home() {
  const { routeTo } = useRouter()
  const [position, setPosition] = useState<google.maps.LatLngLiteral | null>(null)

  const isLogin = useAtomValue(isLoginAtom)
  const user = useAtomValue(userAtom)
  const mapRef = useMapRef()

  let watchId: number

  const userInfo = {
    nickname: user.nickname,
    imageUrl: user.imageUrl,
    totalWalkLog: user.totalWalkLog,
    totalWalkLogContent: user.totalWalkLogContent,
  }

  const handleStartClick = () => {
    routeTo('/onwalk')
  }

  const isSamePosition = (
    a: google.maps.LatLngLiteral | null,
    b: google.maps.LatLngLiteral | null
  ) => a?.lat === b?.lat && a?.lng === b?.lng

  const watchCurrentPosition = () => {
    console.log('워치포지션')
    watchId = navigator.geolocation.watchPosition(
      newPosition => {
        const { latitude, longitude } = newPosition.coords
        const watchedPosition = { lat: latitude, lng: longitude }
        if (!isSamePosition(position, watchedPosition)) {
          setPosition({ lat: latitude, lng: longitude })
        }
      },
      error => console.log(error),
      { enableHighAccuracy: true, maximumAge: 0, timeout: 5000 }
    )
  }

  useEffect(() => {
    watchCurrentPosition()
    return () => {
      console.log('클리어')
      navigator.geolocation.clearWatch(watchId)
    }
  }, [])

  return (
    <div className={styles.container}>
      <HomeHeader isLogin={isLogin} userInfo={userInfo} />
      <MapCanvas ref={mapRef} style={{ width: '100%', height: '100dvh' }} position={position} />
      <button className={styles.startBtn} type='button' onClick={handleStartClick}>
        걷기 시작
      </button>
    </div>
  )
}
