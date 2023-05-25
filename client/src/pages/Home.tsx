import { useState, useEffect } from 'react'
import { useAtomValue } from 'jotai'
import { userAtom, isLoginAtom } from '../store/authAtom'
import { startWalkLog } from '../apis/walkLog'
import useMapRef from '../hooks/useMapRef'
import useRouter from '../hooks/useRouter'
import LiveMap, { MapSize } from '../components/common/Map/LiveMap'
import HomeHeader from '../components/header/HomeHeader'
import { getDistanceBetweenPosition } from '../utils/position'
import styles from './Home.module.scss'

export default function Home() {
  const { routeTo } = useRouter()
  const [position, setPosition] = useState<google.maps.LatLngLiteral | null>(null)

  const isLogin = useAtomValue(isLoginAtom)
  const user = useAtomValue(userAtom)

  const mapRef = useMapRef()

  const userInfo = {
    nickname: user.nickname,
    imageUrl: user.imageUrl,
    totalWalkLog: user.totalWalkLog,
    totalWalkLogContent: user.totalWalkLogContent,
  }

  const handleStartClick = async () => {
    if (!isLogin) {
      console.log('비로그인 시작 x')
      return
    }
    const { walkLogId } = await startWalkLog(user.memberId)
    if (walkLogId === -1) {
      console.log('시작 실패')
      return
    }
    routeTo(`/onwalk/${walkLogId}`)
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
    <div className={styles.container}>
      <HomeHeader isLogin={isLogin} userInfo={userInfo} />
      {position ? (
        <LiveMap ref={mapRef} mapSize={MapSize.LARGE} path={[position]} />
      ) : (
        <div>현위치 찾는 중</div>
      )}

      <button className={styles.startBtn} type='button' onClick={handleStartClick}>
        걷기 시작하기
      </button>
    </div>
  )
}
