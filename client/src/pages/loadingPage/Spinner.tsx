import { useEffect } from 'react'
import styles from './Spinner.module.scss'
import characterIcon from '../../assets/icon_no_bg_180.png'

type StartTextArrElementType = {
  id: number
  text: string
}

type SpinnerProps = {
  startTextArr?: StartTextArrElementType[]
  textIndex?: number
  setTextIndex?: React.Dispatch<React.SetStateAction<number>>
}

export default function Spinner({ startTextArr, textIndex, setTextIndex }: SpinnerProps) {
  function getRandomIndex(length: number) {
    return Math.floor(Math.random() * length)
  }
  useEffect(() => {
    if (startTextArr) {
      const randomIndex = getRandomIndex(startTextArr.length)
      setTextIndex?.(randomIndex)
    }
  }, [])
  return (
    <div className={styles.box}>
      <div className={styles.imgWrapper}>
        <img src={characterIcon} alt='loading' />
      </div>
      <p>
        {startTextArr ? (
          <div>{startTextArr.find(el => el.id === textIndex)?.text}</div>
        ) : (
          <span className={styles.label}>사용자 체크 중...</span>
        )}
      </p>
    </div>
  )
}
