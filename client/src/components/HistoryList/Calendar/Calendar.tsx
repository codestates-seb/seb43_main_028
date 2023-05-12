import styles from './Calendar.module.scss'
import WeekDays from './WeekDays'
import YearMonth from './YearMonth'

interface ItemITF {
  id: number
  snapTime: string
  imageUrl: string
  text: string
}

interface DataITF {
  id: number
  mapImg: string
  createdAt: string
  time: string
  message: string
  walkLogContents: ItemITF[]
}

type CalendarProps = {
  data: DataITF[]
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

  return rows
}

export default function Calendar({ data }: CalendarProps) {
  const now = new Date()
  const Year = now.getFullYear()
  const Month = now.getMonth() + 1
  const Dates = data?.reduce((acc: string[], cur) => {
    const strDate = new Date(cur.createdAt).toDateString()
    return acc.includes(strDate) ? acc : [...acc, strDate]
  }, [])

  const key = [999, 998, 997, 996, 995, 994, 993]

  return (
    <div className={styles.container}>
      <table className={styles.table}>
        <YearMonth year={Year} month={Month} />
        <WeekDays />
        <tbody>
          {getCalendarRows(Year, Month).map((row, idx) => {
            const today = new Date().toDateString()

            return (
              <tr key={key[idx]}>
                {row.map((date, index) => {
                  if (date === 0) return <td key={key[index]} />

                  return (
                    <td key={date.getDate()} className={styles.date}>
                      {date.getDate()}
                      {Dates.map(historyDate => {
                        if (historyDate === date.toDateString()) {
                          return <div key={historyDate} className={styles.dot} />
                        }
                        return ''
                      })}
                      {today === date.toDateString() && <div className={styles.today}>today</div>}
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
