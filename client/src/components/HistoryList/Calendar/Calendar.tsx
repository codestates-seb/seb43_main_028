import { useEffect, useState } from 'react'
import { useAtom } from 'jotai'
import { useQueryClient } from '@tanstack/react-query'
import {
  addMonths,
  subMonths,
  startOfDay,
  isEqual,
  getYear,
  getMonth,
} from '../../../utils/date-fns'
import styles from './Calendar.module.scss'
import WeekDays from './WeekDays'
import YearMonth from './YearMonth'
import Dates from './Dates'
import { userAtom } from '../../../store/authAtom'
import { getHistoryCalendarList } from '../../../apis/history'

type MonthHistoriesType = {
  createdAt: string
  walkLogId: number
}[]

type CalendarProps = {
  date: Date
  setDate: React.Dispatch<React.SetStateAction<Date>>
}

export default function Calendar({ date, setDate }: CalendarProps) {
  const [data, setData] = useState<MonthHistoriesType>([])
  const [selectDate, setSelectDate] = useState<Date | null>(null)
  const [user] = useAtom(userAtom)

  const queryClient = useQueryClient()

  useEffect(() => {
    const getList = async () => {
      const response = await getHistoryCalendarList(
        user.memberId,
        getYear(date),
        getMonth(date) + 1
      )
      setData(response)
      queryClient.invalidateQueries(['history'])
    }
    getList()
  }, [date])

  const histories = data.reduce((acc: number[], cur) => {
    const timeNum = startOfDay(new Date(cur.createdAt)).getTime()
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
          handleSelect={handleSelectDate}
          histories={histories}
        />
      </table>
    </div>
  )
}
