import { useState, useEffect } from 'react'
import { useAtomValue } from 'jotai'
import { userInfoAtom } from '../store/authAtom'
import { startWalkLog } from '../apis/walkLog'
import useMapRef from '../hooks/useMapRef'
import useRouter from '../hooks/useRouter'
import { getDistanceBetweenPosition } from '../utils/position'
import LiveMap, { MapSize } from '../components/common/Map/LiveMap'
import HomeHeader from '../components/header/HomeHeader'
import PwaCarousel from '../components/Home/PwaCarousel'
import StartMessage from '../components/Home/StartMessage'
import styles from './Home.module.scss'

export default function Home() {
  const userInfo = useAtomValue(userInfoAtom)
  const { routeTo } = useRouter()
  const mapRef = useMapRef()
  const [position, setPosition] = useState<google.maps.LatLngLiteral | null>(null)
  const [showMessage, setShowMessage] = useState(false)

  const handleStartClick = async () => {
    if (!userInfo) return
    setShowMessage(true)

    setTimeout(async () => {
      if (userInfo.recordingWalkLogId) {
        return routeTo(`/onwalk/${userInfo.recordingWalkLogId}`)
      }
      const { walkLogId } = await startWalkLog(userInfo.memberId)
      if (walkLogId === -1) {
        setShowMessage(false)
        return
      }
      routeTo(`/onwalk/${walkLogId}`)
    }, 3000)
  }

  const watchCurrentPosition = () => {
    const watchId = navigator.geolocation.watchPosition(
      newPosition => {
        const { latitude, longitude } = newPosition.coords
        const watchedPosition = { lat: latitude, lng: longitude }
        if (position === null || getDistanceBetweenPosition(position, watchedPosition) > 2) {
          setPosition(watchedPosition)
        }
      },
      error => console.log(error),
      { enableHighAccuracy: true, maximumAge: 0, timeout: 5000 }
    )

    return {
      watchId,
      clearWatchPosition: () => {
        navigator.geolocation.clearWatch(watchId)
      },
    }
  }

  useEffect(() => {
    const { clearWatchPosition } = watchCurrentPosition()
    return () => {
      clearWatchPosition()
    }
  }, [])

  return (
    <>
      <PwaCarousel />
      {showMessage && <StartMessage />}
      <div className={styles.container}>
        <HomeHeader userInfo={userInfo} />
        <LiveMap ref={mapRef} mapSize={MapSize.LARGE} path={position ? [position] : null} />

        {!userInfo ? (
          <button className={styles.nologinBtn} type='button' disabled>
            걷기는 로그인 후 가능합니다.
          </button>
        ) : (
          <button className={styles.startBtn} type='button' onClick={handleStartClick}>
            {userInfo?.recordingWalkLogId ? '걷기 계속하기(미종료)' : '걷기 시작하기'}
          </button>
        )}
      </div>
    </>
  )
}
