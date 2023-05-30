import { useState, useEffect } from 'react'
import { useAtomValue } from 'jotai'
import { userInfoAtom } from '../store/authAtom'
import { startWalkLog } from '../apis/walkLog'
import useMapRef from '../hooks/useMapRef'
import useRouter from '../hooks/useRouter'
import LiveMap, { MapSize } from '../components/common/Map/LiveMap'
import HomeHeader from '../components/header/HomeHeader'
import { getDistanceBetweenPosition } from '../utils/position'
import styles from './Home.module.scss'
import PwaCarousel from '../components/Home/PwaCarousel'
import { differenceInMilliseconds } from '../utils/date-fns'
import android1 from '../assets/pwa-android-1.png'
import android2 from '../assets/pwa-android-2.png'
import android3 from '../assets/pwa-android-3.png'
import ios1 from '../assets/pwa-ios-1.png'
import ios2 from '../assets/pwa-ios-2.png'
import ios3 from '../assets/pwa-ios-3.png'

const IOSCarousel = [
  {
    id: 0,
    src: ios1,
    text: '1. 공유 버튼을 클릭합니다.',
  },
  {
    id: 1,
    src: ios2,
    text: '2. ‘홈 화면에 추가’ 버튼을 클릭합니다.',
  },
  {
    id: 2,
    src: ios3,
    text: '3. 기기에 바로가기 아이콘이 추가됩니다.',
  },
]

const AndroidCarousel = [
  {
    id: 0,
    src: android1,
    text: '1. 설정 버튼을 클릭합니다.',
  },
  {
    id: 1,
    src: android2,
    text: '2. ‘앱 설치’ 버튼을 클릭합니다.',
  },
  {
    id: 2,
    src: android3,
    text: '3. 기기에 바로가기 아이콘이 추가됩니다.',
  },
]

const userOs = navigator.userAgent.replace(/ /g, '').toLowerCase()

const getCarousel = () => {
  if (/android/i.test(userOs)) {
    return AndroidCarousel
  }
  if (/(iphone|ipad)/i.test(userOs)) {
    return IOSCarousel
  }

  return null
}

const isLocalStorageExpired = () => {
  const expires = localStorage.getItem('pwa-carousel-expires')
  if (!expires) {
    return true
  }
  if (differenceInMilliseconds(new Date(), new Date(expires)) >= 0) {
    localStorage.removeItem('pwa-carousel-expires')
    return true
  }
  return false
}

export default function Home() {
  const userInfo = useAtomValue(userInfoAtom)
  const { routeTo } = useRouter()
  const mapRef = useMapRef()
  const [position, setPosition] = useState<google.maps.LatLngLiteral | null>(null)
  const [pwaModalOpen, setPwaModalOpen] = useState<boolean>(isLocalStorageExpired())

  const handleStartClick = async () => {
    if (!userInfo) return
    const { walkLogId } = await startWalkLog(userInfo.memberId)
    if (walkLogId === -1) return
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

  const handlePwaModalClose = () => {
    setPwaModalOpen(false)
  }

  useEffect(() => {
    const { clearWatchPosition } = watchCurrentPosition()
    return () => {
      clearWatchPosition()
    }
  }, [])

  return (
    <>
      {userInfo && getCarousel() && pwaModalOpen && (
        <PwaCarousel handleClose={handlePwaModalClose} carousel={getCarousel()} />
      )}
      <div className={styles.container}>
        <HomeHeader userInfo={userInfo} />
        {position ? (
          <LiveMap ref={mapRef} mapSize={MapSize.LARGE} path={[position]} />
        ) : (
          <div>현위치 찾는 중</div>
        )}

        <button className={styles.startBtn} type='button' onClick={handleStartClick}>
          걷기 시작하기
        </button>
      </div>
    </>
  )
}
