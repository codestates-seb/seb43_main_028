import { useEffect, useState } from 'react'
import Icon from '../common/Icon'
import styles from './PwaCarousel.module.scss'
import { addDays } from '../../utils/date-fns'

export type CarouselType = {
  id: number
  src: string
  text: string
}[]

type PwaCarouselProps = {
  handleClose: () => void
  carousel: CarouselType | null
}

export default function PwaCarousel({ handleClose, carousel }: PwaCarouselProps) {
  const [currentIdx, setCurrentIdx] = useState(0)
  const [style, setStyle] = useState({ transform: 'translate(0)' })
  const [aWeekClose, setaWeekClose] = useState(false)

  const handleWeekCloseCheck = () => {
    setaWeekClose(prev => !prev)
  }

  useEffect(() => {
    setStyle({ transform: `translate(-${currentIdx}00%)` })
    const afterAWeek = addDays(new Date(), 7)
    if (aWeekClose) {
      localStorage.setItem('pwa-carousel-expires', afterAWeek.toString())
    } else {
      localStorage.removeItem('pwa-carousel-expires')
    }
  }, [currentIdx, aWeekClose])

  return (
    <div className={styles.container}>
      <div className={styles.modal}>
        <div className={styles.title}>
          <div> 앱 설치해서 사용하기</div>
          <button type='button' onClick={handleClose}>
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
              <button type='button' onClick={() => setCurrentIdx(prev => prev - 1)}>
                <Icon name='arrow-left' size={48} />
              </button>
            ) : (
              <div className={styles.noBtn} />
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
              <button type='button' onClick={() => setCurrentIdx(prev => prev + 1)}>
                <Icon name='arrow-right' size={48} />
              </button>
            ) : (
              <div className={styles.noBtn} />
            )}
          </div>
        </div>
      </div>
    </div>
  )
}
