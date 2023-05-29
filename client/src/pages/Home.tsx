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
import { CarouselType } from '../types/Carousel'
import { differenceInMilliseconds } from '../utils/date-fns'

const IOSCarousel = [
  {
    id: 0,
    src: 'https://velog.velcdn.com/images/iberis/post/4b8f217f-4512-465b-9544-e2738918d457/image.png',
    text: '1. 공유 버튼을 클릭합니다.',
  },
  {
    id: 1,
    src: 'https://velog.velcdn.com/images/iberis/post/56225e7a-da58-46f2-959a-c354533fc12f/image.png',
    text: '2. ‘홈 화면에 추가’ 버튼을 클릭합니다.',
  },
  {
    id: 2,
    src: 'https://velog.velcdn.com/images/iberis/post/1e3207fc-cc24-4755-afa6-936a597c2627/image.png',
    text: '3. 기기에 바로가기 아이콘이 추가됩니다.',
  },
]

const AndroidCarousel = [
  {
    id: 0,
    src: 'https://velog.velcdn.com/images/iberis/post/a71eec2f-c7a4-45c7-a20c-a8105d3be273/image.png',
    text: '1. 설정 버튼을 클릭합니다.',
  },
  {
    id: 1,
    src: 'https://velog.velcdn.com/images/iberis/post/5093c305-2ed3-4733-9551-c359dd319472/image.png',
    text: '2. ‘앱 설치’ 버튼을 클릭합니다.',
  },
  {
    id: 2,
    src: 'https://velog.velcdn.com/images/iberis/post/e687ebcd-5510-4e34-b840-e91810207766/image.png',
    text: '3. 기기에 바로가기 아이콘이 추가됩니다.',
  },
]

export default function Home() {
  const userInfo = useAtomValue(userInfoAtom)
  const { routeTo } = useRouter()
  const mapRef = useMapRef()
  const [position, setPosition] = useState<google.maps.LatLngLiteral | null>(null)
  const [pwaModalOpen, setPwaModalOpen] = useState<boolean>(false)
  const [carousel, setCarousel] = useState<CarouselType>()

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

  const getUserOS = () => {
    const userOs = navigator.userAgent.replace(/ /g, '').toLowerCase()
    if (/android/i.test(userOs)) {
      setCarousel(AndroidCarousel)
      return setPwaModalOpen(true)
    }
    if (/(iphone|ipad)/i.test(userOs)) {
      setCarousel(IOSCarousel)
      return setPwaModalOpen(true)
    }

    return setPwaModalOpen(false)
  }

  useEffect(() => {
    const { clearWatchPosition } = watchCurrentPosition()
    const expires = localStorage.getItem('pwa-carousel-expires')
    if (!expires) {
      getUserOS()
    } else if (differenceInMilliseconds(new Date(), new Date(expires)) >= 0) {
      localStorage.removeItem('pwa-carousel-expires')
      getUserOS()
    }
    return () => {
      clearWatchPosition()
    }
  }, [])

  return (
    <>
      {pwaModalOpen && <PwaCarousel handleClose={handlePwaModalClose} carousel={carousel} />}
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
