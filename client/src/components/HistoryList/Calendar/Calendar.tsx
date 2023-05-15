import { useState } from 'react'
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

function getCalendarRows(year: number, month: number): (0 | Date)[][] {
  const lastDate = new Date(year, month, 0).getDate()
  const startBlankCount = new Date(year, month - 1, 1).getDay()
  const endBlankCount = 7 - ((startBlankCount + lastDate) % 7)
  const rowCount = (lastDate + startBlankCount + endBlankCount) / 7

  const allDates = [
    ...Array(startBlankCount).fill(0),
    ...Array(lastDate)
      .fill(0)
      .map((_, i) => new Date(year, month - 1, i + 1)),
  ]

  const rows = Array(rowCount)
    .fill(0)
    .map((_, i) => [...allDates].splice(i * 7, 7))

  for (let i = 0; i < endBlankCount; i += 1) {
    rows[rows.length - 1].push(0)
  }

  return rows
}

export default function Calendar({ data }: CalendarProps) {
  const now = new Date()
  const [year, setYear] = useState(now.getFullYear())
  const [month, setMonth] = useState(now.getMonth() + 1)

  const Dates = data?.reduce((acc: string[], cur) => {
    const strDate = new Date(cur.startAt).toDateString()
    return acc.includes(strDate) ? acc : [...acc, strDate]
  }, [])

  const handlePrevMonth = () => {
    if (month <= 1) {
      setYear(year - 1)
      setMonth(12)
    } else {
      setMonth(month - 1)
    }
  }
  const handleNextMonth = () => {
    if (month >= 12) {
      setYear(year + 1)
      setMonth(1)
    } else {
      setMonth(month + 1)
    }
  }

  return (
    <div className={styles.container}>
      <table className={styles.table}>
        <YearMonth
          year={year}
          month={month}
          prevMonth={handlePrevMonth}
          nextMonth={handleNextMonth}
        />
        <WeekDays />
        <tbody>
          {getCalendarRows(year, month).map(row => {
            const today = new Date().toDateString()

            return (
              <tr key={crypto.randomUUID()}>
                {row.map(date => {
                  if (date === 0) return <td key={crypto.randomUUID()} className={styles.date} />

                  return (
                    <td key={date.getDate()} className={styles.date}>
                      {date.getDate()}
                      {today === date.toDateString() && <div className={styles.today} />}
                      {Dates.map(historyDate => {
                        if (historyDate === date.toDateString()) {
                          return <div key={historyDate} className={styles.dot} />
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
