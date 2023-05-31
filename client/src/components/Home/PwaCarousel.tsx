import { useEffect, useState } from 'react'
import Icon from '../common/Icon'
import styles from './PwaCarousel.module.scss'
import { addDays, differenceInMilliseconds } from '../../utils/date-fns'
import android1 from '../../assets/pwa-android-1.png'
import android2 from '../../assets/pwa-android-2.png'
import android3 from '../../assets/pwa-android-3.png'
import ios1 from '../../assets/pwa-ios-1.png'
import ios2 from '../../assets/pwa-ios-2.png'
import ios3 from '../../assets/pwa-ios-3.png'

const IOSCarousel = [
  { id: 0, src: ios1, text: '1. 공유 버튼을 클릭합니다.' },
  { id: 1, src: ios2, text: '2. ‘홈 화면에 추가’ 버튼을 클릭합니다.' },
  { id: 2, src: ios3, text: '3. 기기에 바로가기 아이콘이 추가됩니다.' },
]
const AndroidCarousel = [
  { id: 0, src: android1, text: '1. 설정 버튼을 클릭합니다.' },
  { id: 1, src: android2, text: '2. ‘앱 설치’ 버튼을 클릭합니다.' },
  { id: 2, src: android3, text: '3. 기기에 바로가기 아이콘이 추가됩니다.' },
]

const getCarousel = (userOs: string) => {
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

export default function PwaCarousel() {
  const [isShow, setIsShow] = useState<boolean>(isLocalStorageExpired())
  const [currentIdx, setCurrentIdx] = useState(0)
  const [style, setStyle] = useState({ transform: 'translate(0)' })
  const [aWeekClose, setaWeekClose] = useState(false)

  const userOS = navigator.userAgent.replace(/ /g, '').toLowerCase()
  const carousel = getCarousel(userOS)

  const handleWeekCloseCheck = () => {
    setaWeekClose(prev => !prev)
    if (aWeekClose === false) {
      const afterAWeek = addDays(new Date(), 7)
      localStorage.setItem('pwa-carousel-expires', afterAWeek.toString())
    }
  }

  useEffect(() => {
    setStyle({ transform: `translate(-${currentIdx}00%)` })
  }, [currentIdx, aWeekClose])

  if (carousel === null || isShow === false) return null

  return (
    <div className={styles.container}>
      <div className={styles.modal}>
        <div className={styles.title}>
          <div>앱 설치해서 사용하기</div>
          <button type='button' onClick={() => setIsShow(false)}>
            <div className={styles.close}>닫기</div>
          </button>
        </div>

        <div className={styles.aWeekClose}>
          <button type='button' onClick={handleWeekCloseCheck}>
            {aWeekClose ? (
              <Icon name='after-check' size={24} />
            ) : (
              <Icon name='before-check' size={24} />
            )}
          </button>
          <div>7일간 이 창을 띄우지 않습니다.</div>
        </div>

        <div className={styles.carousel}>
          <div className={styles.carouselBtnImgBox}>
            {currentIdx > 0 ? (
              <button
                type='button'
                className={styles.leftBtn}
                onClick={() => setCurrentIdx(prev => prev - 1)}
              >
                <Icon name='arrow-left' size={32} />
              </button>
            ) : (
              <div className={styles.leftNoBtn} />
            )}

            <div className={styles.imgContainer}>
              <div className={styles.flexBox} style={style}>
                {carousel?.map(content => {
                  return (
                    <div key={content.id}>
                      <img src={content.src} className={styles.img} alt='홈 화면에 추가 방법' />
                      <div className={styles.carouselText}>{content.text}</div>
                    </div>
                  )
                })}
              </div>
            </div>

            {carousel && currentIdx < carousel.length - 1 ? (
              <button
                type='button'
                className={styles.rightBtn}
                onClick={() => setCurrentIdx(prev => prev + 1)}
              >
                <Icon name='arrow-right' size={32} />
              </button>
            ) : (
              <div className={styles.rightNoBtn} />
            )}
          </div>
        </div>
      </div>
    </div>
  )
}
