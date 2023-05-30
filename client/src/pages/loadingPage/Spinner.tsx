import styles from './Spinner.module.scss'
import characterIcon from '../../assets/icon_no_bg_180.png'

type SpinnerProps = {
  label?: string
  hasBg?: boolean
}
export default function Spinner({ label, hasBg = true }: SpinnerProps) {
  return (
    <div className={styles.box} style={hasBg ? { backgroundColor: '#f3f4f6' } : {}}>
      <div className={styles.imgWrapper}>
        <img src={characterIcon} alt='loading' />
      </div>
      <p className={styles.label}>{label}</p>
    </div>
  )
}
