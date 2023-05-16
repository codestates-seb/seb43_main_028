import styles from './Dates.module.scss'
import { getWeekRows, startOfToday, isEqual, format } from './date-fns'

type SelectedDateProps = {
  day: Date
  histories: number[]
  handleSelectDate: (day: Date) => void
}

function SelectedDate({ day, histories, handleSelectDate }: SelectedDateProps) {
  return (
    <button type='button' onClick={() => handleSelectDate(day)} className={styles.selected}>
      {format(day, 'dd')}
      {isEqual(startOfToday(), day) && <div className={styles.today} />}
      {histories.map(history => {
        if (isEqual(history, day)) {
          return <div key={history} className={styles.dot} />
        }
        return ''
      })}
    </button>
  )
}

function UnSelectedDate({ day, histories, handleSelectDate }: SelectedDateProps) {
  return (
    <button type='button' onClick={() => handleSelectDate(day)}>
      {format(day, 'dd')}
      {isEqual(startOfToday(), day) && <div className={styles.today} />}
      {histories.map(history => {
        if (isEqual(history, day)) {
          return <div key={history} className={styles.dot} />
        }
        return ''
      })}
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
                <td key={crypto.randomUUID()} className={styles.date}>
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
