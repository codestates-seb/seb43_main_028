import styles from './Toggle.module.scss'

type ToggleProps = {
  calendar: boolean
  handleCalendar: () => void
}

export default function Toggle({ calendar, handleCalendar }: ToggleProps) {
  return (
    <div className={styles.toggleBox}>
      <button
        type='button'
        className={calendar ? styles.btn : styles.clickedBtn}
        onClick={handleCalendar}
      >
        최신순 보기
      </button>
      <button
        type='button'
        className={calendar ? styles.clickedBtn : styles.btn}
        onClick={handleCalendar}
      >
        월별 보기
      </button>
    </div>
  )
}
