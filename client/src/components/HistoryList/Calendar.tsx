import Icon from '../common/Icon'
import styles from './Calendar.module.scss'

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

  const weekdays = ['일', '월', '화', '수', '목', '금', '토']

  return (
    <div className={styles.container}>
      <table className={styles.table}>
        <caption>
          <div className={styles.captionBox}>
            <button type='button'>
              <Icon name='arrow-left' size={16} />
            </button>
            {Year}년 {Month}월
            <button type='button'>
              <Icon name='arrow-right' size={24} />
            </button>
          </div>
        </caption>
        <thead>
          <tr>
            {weekdays.map(weekday => {
              let day
              if (weekday === '일') {
                day = 'sunday'
              } else if (weekday === '토') {
                day = 'saturday'
              } else {
                day = 'day'
              }
              return (
                <td key={weekday} className={styles[day]}>
                  {weekday}
                </td>
              )
            })}
          </tr>
        </thead>
        <tbody>
          {getCalendarRows(date, Year, Month).map(row => {
            return (
              <tr key={row[0] ? row[0].getDate() : row[0]}>
                {row.map((dt, index) => {
                  if (dt === 0) return <td key={weekdays[index]}> </td>
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
