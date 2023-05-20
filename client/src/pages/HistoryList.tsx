import { useCallback, useRef, useState } from 'react'
import { useInfiniteQuery } from '@tanstack/react-query'
import { useAtom } from 'jotai'
import styles from './HistoryList.module.scss'
import History from '../components/HistoryList/History'
import Calendar from '../components/HistoryList/Calendar/Calendar'
import Toggle from '../components/HistoryList/Toggle'
import { getHistoryList } from '../apis/history'
import { userAtom } from '../store/authAtom'
import { HistoryListDataType } from '../types/History'
import { getDate, getMonth, getYear } from '../utils/date-fns'

export default function HistoryList() {
  const [calendar, setCalendar] = useState<boolean>(false)
  const [date, setDate] = useState<Date>(new Date())
  const [user] = useAtom(userAtom)

  const handleCalendar = () => {
    setCalendar(!calendar)
  }

  const { isLoading, isError, data, isFetchingNextPage, hasNextPage, fetchNextPage } =
    useInfiniteQuery({
      queryKey: [
        'history',
        calendar,
        date,
        {
          memberId: user.memberId,
          year: getYear(date),
          month: getMonth(date) + 1,
          day: getDate(date),
        },
      ],
      queryFn: ({ pageParam, queryKey }) => {
        if (calendar) {
          const { year, month } = queryKey[3] as { year: number; month: number }
          return getHistoryList(user.memberId, pageParam, year, month)
        }
        return getHistoryList(user.memberId, pageParam)
      },
      getNextPageParam: lastPage => {
        const { page, totalPages } = lastPage.pageInfo
        return page < totalPages ? page + 1 : undefined
      },
    })

  const intObserver = useRef<IntersectionObserver>()
  const lastHistoryRef = useCallback(
    (history: HTMLDivElement) => {
      if (isFetchingNextPage) return
      if (intObserver.current) intObserver.current.disconnect()

      intObserver.current = new IntersectionObserver(histories => {
        if (histories[0].isIntersecting && hasNextPage) {
          console.log('마지막에 접근')
          fetchNextPage()
        }
      })

      if (history) {
        intObserver.current.observe(history)
      }
    },
    [isFetchingNextPage, fetchNextPage, hasNextPage]
  )

  if (isLoading) return <h1>Loading...</h1>
  if (isFetchingNextPage) return <h1>Loading...</h1>
  if (isError) return <h1>Fail...</h1>

  const body = data?.pages.map(page => {
    return page.data.map((history: HistoryListDataType, index: number) => {
      if (page.data.length === index + 1) {
        return <History key={history.walkLogId} data={history} ref={lastHistoryRef} />
      }
      return <History key={history.walkLogId} data={history} />
    })
  })

  return (
    <div>
      <Toggle handleCalendar={handleCalendar} calendar={calendar} />
      {calendar && <Calendar date={date} setDate={setDate} />}
      <ul className={styles.historyList}>{body}</ul>
    </div>
  )
}
