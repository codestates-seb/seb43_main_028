import { useEffect } from 'react'
import styles from './Landing.module.scss'
import icon from '../../assets/icon_no_bg_180.png'

type LandingProps = {
  onInitialLoad?: () => void
}

export default function Landing({ onInitialLoad }: LandingProps) {
  useEffect(() => {
    setTimeout(() => {
      onInitialLoad?.()
    }, 2000)
  }, [onInitialLoad])

  return (
    <div className={styles.container}>
      <div className={styles.imgWrapper}>
        <img src={icon} alt='app-character' />
      </div>
    </div>
  )
}
