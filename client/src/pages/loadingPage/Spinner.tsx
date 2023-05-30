import styles from './Spinner.module.scss'
import characterIcon from '../../assets/icon_no_bg_180.png'

type SpinnerProps = {
  label?: string
}
export default function Spinner({ label }: SpinnerProps) {
  return (
    <div className={styles.box}>
      <div className={styles.imgWrapper}>
        <img src={characterIcon} alt='loading' />
      </div>
      <p className={styles.label}>{label}</p>
    </div>
  )
}
