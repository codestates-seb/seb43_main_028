import { useState, useEffect } from 'react'
import { useAtomValue } from 'jotai'
import { userAtom, isLoginAtom } from '../store/authAtom'
import useMapRef from '../hooks/useMapRef'
import MapCanvas from '../components/common/Map/MapCanvas'
import HomeHeader from '../components/Home/HomeHeader'
import useRouter from '../hooks/useRouter'
import styles from './Home.module.scss'
import { startWalkLog } from '../apis/walkLog'

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

  const startWalkLogHandler = async () => {
    if (!isLogin) return
    const walkLogId = await startWalkLog({ memberId: user.memberId })
    if (!walkLogId) return
    // TODO : walkLogId를 전역으로 저장해서 onwalk 페이지에서 사용해야 한다.
    // TODO : 로그인/비로그인 ID를 분기해야한다.
    routeTo('/onwalk')
  }

  const handleStartClick = () => {
    startWalkLogHandler()
  }

  const isSamePosition = (
    a: google.maps.LatLngLiteral | null,
    b: google.maps.LatLngLiteral | null
  ) => a?.lat === b?.lat && a?.lng === b?.lng

  const watchCurrentPosition = () => {
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
      navigator.geolocation.clearWatch(watchId)
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
