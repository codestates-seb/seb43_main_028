import styles from './Dates.module.scss'
import { startOfToday, isEqual, format } from '../../../utils/date-fns'
import { getWeekRows } from '../../../utils/date'

type SelectedDateProps = {
  day: Date
  histories: number[]
  handleSelect: (day: Date) => void
}

function SelectedDate({ day, histories, handleSelect }: SelectedDateProps) {
  const isHistory = histories.map(history => {
    if (isEqual(history, day)) {
      return <div key={history} className={styles.dot} />
    }
    return ''
  })

  return (
    <button type='button' onClick={() => handleSelect(day)} className={styles.selected}>
      {format(day, 'dd')}
      {isEqual(startOfToday(), day) && <div className={styles.today} />}
      {isHistory}
    </button>
  )
}

function UnSelectedDate({ day, histories, handleSelect }: SelectedDateProps) {
  const isHistory = histories.map(history => {
    if (isEqual(history, day)) {
      return <div key={history} className={styles.dot} />
    }
    return ''
  })

  return (
    <button type='button' onClick={() => handleSelect(day)} className={styles.unSelected}>
      {format(day, 'dd')}
      {isEqual(startOfToday(), day) && <div className={styles.today} />}
      {isHistory}
    </button>
  )
}

type DatesProps = {
  date: Date
  selectDate: Date | null
  histories: number[]
  handleSelect: (day: Date) => void
}

export default function Dates({ date, selectDate, handleSelect, histories }: DatesProps) {
  return (
    <tbody>
      {getWeekRows(date).map(week => {
        return (
          <tr key={crypto.randomUUID()}>
            {week.map(day => {
              if (day === 0) return <td key={crypto.randomUUID()} />

              return (
                <td key={crypto.randomUUID()} className={styles.container}>
                  {selectDate && isEqual(day, selectDate) ? (
                    <SelectedDate day={day} histories={histories} handleSelect={handleSelect} />
                  ) : (
                    <UnSelectedDate day={day} histories={histories} handleSelect={handleSelect} />
                  )}
                </td>
              )
            })}
          </tr>
        )
      })}
    </tbody>
  )
}
