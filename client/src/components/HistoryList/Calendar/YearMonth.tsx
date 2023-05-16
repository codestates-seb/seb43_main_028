import { format, ko } from './date-fns'
import Icon from '../../common/Icon'
import styles from './YearMonth.module.scss'

type YearMonthProps = {
  date: Date
  handleMonth: (type: string) => void
}

export default function YearMonth({ date, handleMonth }: YearMonthProps) {
  return (
    <caption>
      <div className={styles.container}>
        <button type='button' onClick={() => handleMonth('previous')}>
          <Icon name='arrow-left' size={24} />
        </button>
        {format(new Date(date), 'yyyy년 M월', { locale: ko })}
        <button type='button' onClick={() => handleMonth('next')}>
          <Icon name='arrow-right' size={24} />
        </button>
      </div>
    </caption>
  )
}
