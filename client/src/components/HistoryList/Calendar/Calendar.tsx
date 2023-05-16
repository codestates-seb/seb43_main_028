import { useState } from 'react'
import {
  addMonths,
  subMonths,
  getWeekRows,
  startOfToday,
  startOfDay,
  isEqual,
  format,
} from './date-fns'
import styles from './Calendar.module.scss'
import WeekDays from './WeekDays'
import YearMonth from './YearMonth'

export type ItemType = {
  id: number
  createdAt: string
  imageUrl: string
  text: string
}

export type DataType = {
  id: number
  mapImg: string
  startAt: string
  endAt: string
  message: string
  walkLogContents: ItemType[]
}

type CalendarProps = {
  data: DataType[]
}

export default function Calendar({ data }: CalendarProps) {
  const [date, setDate] = useState(new Date())

  const histories = data?.reduce((acc: number[], cur) => {
    const timeNum = startOfDay(new Date(cur.startAt)).getTime()
    return acc.includes(timeNum) ? acc : [...acc, timeNum]
  }, [])

  const handleMonth = (type: string) => {
    if (type === 'previous') {
      setDate(prev => subMonths(prev, 1))
    }
    if (type === 'next') {
      setDate(prev => addMonths(prev, 1))
    }
  }

  return (
    <div className={styles.container}>
      <table className={styles.table}>
        <YearMonth date={date} handleMonth={handleMonth} />
        <WeekDays />
        <tbody>
          {getWeekRows(date).map(week => {
            return (
              <tr key={crypto.randomUUID()}>
                {week.map(day => {
                  if (day === 0) return <td key={crypto.randomUUID()} className={styles.date} />

                  return (
                    <td key={day.getDate()} className={styles.date}>
                      {format(day, 'dd')}
                      {isEqual(startOfToday(), day) && <div className={styles.today} />}
                      {histories.map(history => {
                        if (isEqual(history, day)) {
                          return <div key={history} className={styles.dot} />
                        }
                        return ''
                      })}
                    </td>
                  )
                })}
              </tr>
            )
          })}
        </tbody>
      </table>
    </div>
  )
}
