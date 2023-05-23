import styles from './Spinner.module.scss'

export default function Spinner() {
  return (
    <div className={styles.box}>
      <div className={styles.ldsRing}>
        <div />
        <div />
        <div />
        <div />
      </div>
      <p>
        <span className={styles.label}>사용자 체크 중...</span>
      </p>
    </div>
  )
}
