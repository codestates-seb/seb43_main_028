import styles from './Dates.module.scss'
import { getWeekRows, startOfToday, isEqual, format } from './date-fns'

type SelectedDateProps = {
  day: Date
  histories: number[]
  handleSelectDate: (day: Date) => void
}

function SelectedDate({ day, histories, handleSelectDate }: SelectedDateProps) {
  const isHistory = histories.map(history => {
    if (isEqual(history, day)) {
      return <div key={history} className={styles.dot} />
    }
    return ''
  })

  return (
    <button type='button' onClick={() => handleSelectDate(day)} className={styles.selected}>
      {format(day, 'dd')}
      {isEqual(startOfToday(), day) && <div className={styles.today} />}
      {isHistory}
    </button>
  )
}

function UnSelectedDate({ day, histories, handleSelectDate }: SelectedDateProps) {
  const isHistory = histories.map(history => {
    if (isEqual(history, day)) {
      return <div key={history} className={styles.dot} />
    }
    return ''
  })

  return (
    <button type='button' onClick={() => handleSelectDate(day)} className={styles.unSelected}>
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
  handleSelectDate: (day: Date) => void
}

export default function Dates({ date, selectDate, handleSelectDate, histories }: DatesProps) {
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
                    <SelectedDate
                      day={day}
                      histories={histories}
                      handleSelectDate={handleSelectDate}
                    />
                  ) : (
                    <UnSelectedDate
                      day={day}
                      histories={histories}
                      handleSelectDate={handleSelectDate}
                    />
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
