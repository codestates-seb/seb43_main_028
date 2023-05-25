import styles from './Spinner.module.scss'
import characterIcon from '../../assets/icon_no_bg_180.png'

export default function Spinner() {
  return (
    <div className={styles.box}>
      <div className={styles.imgWrapper}>
        <img src={characterIcon} alt='loading' />
      </div>
      {/* <div className={styles.ldsRing}>
        <div />
        <div />
        <div />
        <div />
      </div> */}
      <p>
        <span className={styles.label}>사용자 체크 중...</span>
      </p>
    </div>
  )
}
