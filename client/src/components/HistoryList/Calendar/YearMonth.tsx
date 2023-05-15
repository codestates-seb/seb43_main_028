import Icon from '../../common/Icon'
import styles from './YearMonth.module.scss'

type YearMonthProps = {
  year: number
  month: number
  prevMonth: () => void
  nextMonth: () => void
}

export default function YearMonth({ year, month, prevMonth, nextMonth }: YearMonthProps) {
  return (
    <caption>
      <div className={styles.container}>
        <button type='button' onClick={prevMonth}>
          <Icon name='arrow-left' size={16} />
        </button>
        {year}년 {month}월
        <button type='button' onClick={nextMonth}>
          <Icon name='arrow-right' size={24} />
        </button>
      </div>
    </caption>
  )
}
