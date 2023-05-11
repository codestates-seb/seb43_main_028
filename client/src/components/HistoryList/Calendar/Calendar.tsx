import styles from './Calendar.module.scss'
import WeekDays from './WeekDays'
import YearMonth from './YearMonth'

type CalendarProps = {
  data: string | number
}

function getCalendarRows(date: Date, year: number, month: number): (0 | Date)[][] {
  const lastDate = new Date(year, month + 1, 0).getDate()
  const startBlankCount = date.getDay()
  const endBlankCount = 7 - ((startBlankCount + lastDate) % 7)
  const rowCount = (lastDate + startBlankCount + endBlankCount) / 7

  const allDates = [
    ...Array(startBlankCount).fill(0),
    ...Array(lastDate)
      .fill(0)
      .map((_, i) => new Date(year, month, i + 1)),
  ]

  const rows = Array(rowCount)
    .fill(0)
    .map((_, i) => [...allDates].splice(i * 7, 7))

  return rows
}

export default function Calendar({ data = Date.now() }: CalendarProps) {
  const date = new Date(data)
  const Year = date.getFullYear()
  const Month = date.getMonth()
  const key = [0, 1, 2, 3, 4, 5, 6]

  return (
    <div className={styles.container}>
      <table className={styles.table}>
        <YearMonth year={Year} month={Month} />
        <WeekDays />
        <tbody>
          {getCalendarRows(date, Year, Month).map((row, idx) => {
            return (
              <tr key={key[idx]}>
                {row.map((dt, index) => {
                  if (dt === 0) return <td key={key[index]}> </td>
                  return <td key={dt.getDate()}>{dt.getDate()}</td>
                })}
              </tr>
            )
          })}
        </tbody>
      </table>
    </div>
  )
}
