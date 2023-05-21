import { useState, useEffect } from 'react'
import { useAtomValue } from 'jotai'
import { userAtom, isLoginAtom } from '../store/authAtom'
import { startWalkLog } from '../apis/walkLog'
import useMapRef from '../hooks/useMapRef'
import useRouter from '../hooks/useRouter'
import MapCanvas from '../components/common/Map/MapCanvas'
import HomeHeader from '../components/header/HomeHeader'
import { isSamePosition } from '../utils/position'
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
    // if (!isLogin) {
    //   // TODO : 비로그인 시 동작
    //   return
    // }
    const { walkLogId } = await startWalkLog(user.memberId)
    if (walkLogId === -1) {
      // TODO : 시작 실패 메시지
      console.log('시작 실패')
      return
    }
    routeTo('/onwalk')
  }

  const watchCurrentPosition = () => {
    const watchId = navigator.geolocation.watchPosition(
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
        <MapCanvas ref={mapRef} style={{ width: '100%', height: '100dvh' }} position={position} />
      ) : (
        // TODO : 로딩 컴포넌트 만들기
        <div>
          현위치 찾는 중----------------------------현위치 찾는 중----------------------------현위치
          찾는 중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------현위치 찾는
          중----------------------------현위치 찾는 중----------------------------
        </div>
      )}

      <button className={styles.startBtn} type='button' onClick={handleStartClick}>
        걷기 시작
      </button>
    </div>
  )
}
