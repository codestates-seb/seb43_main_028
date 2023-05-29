import { useEffect } from 'react'
import styles from './Landing.module.scss'
import icon from '../assets/icon_no_bg_180.png'

export default function Landing({ onInitialLoad }: { onInitialLoad: () => void }) {
  // Perform any initial loading logic here
  // Once the initial load is complete, call `onInitialLoad` callback
  // to notify the App component
  useEffect(() => {
    // Simulating a delay of 1 second for the initial load
    setTimeout(() => {
      onInitialLoad()
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
