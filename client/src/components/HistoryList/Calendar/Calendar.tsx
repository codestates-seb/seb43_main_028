import { useState } from 'react'
import { addMonths, subMonths, startOfDay, isEqual } from './date-fns'
import styles from './Calendar.module.scss'
import WeekDays from './WeekDays'
import YearMonth from './YearMonth'
import Dates from './Dates'

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
  const [date, setDate] = useState<Date>(new Date())
  const [selectDate, setSelectDate] = useState<Date | null>(null)

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

  const handleSelectDate = (day: Date) => {
    if (selectDate && isEqual(day, selectDate)) {
      return setSelectDate(null)
    }
    return setSelectDate(day)
  }

  return (
    <div className={styles.container}>
      <table className={styles.table}>
        <YearMonth date={date} handleMonth={handleMonth} />
        <WeekDays />
        <Dates
          date={date}
          selectDate={selectDate}
          handleSelectDate={handleSelectDate}
          histories={histories}
        />
      </table>
    </div>
  )
}
