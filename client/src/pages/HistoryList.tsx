import { useCallback, useRef, useState } from 'react'
import { useInfiniteQuery } from '@tanstack/react-query'
import { useAtom } from 'jotai'
import styles from './HistoryList.module.scss'
import History from '../components/HistoryList/History'
import Calendar from '../components/HistoryList/Calendar/Calendar'
import Toggle from '../components/HistoryList/Toggle'
import { getHistoryList } from '../apis/history'
import { isLoginAtom, userAtom } from '../store/authAtom'
import { HistoryListDataType } from '../types/History'
import { getDate, getMonth, getYear } from '../utils/date-fns'
import HistoryListLoading from './loadingPage/HistoryListLoading'
import HistoryLoading from './loadingPage/HistoryLoading'
import HomeHeader from '../components/header/HomeHeader'

export default function HistoryList() {
  const [calendar, setCalendar] = useState<boolean>(false)
  const [selectDate, setSelectDate] = useState<Date | null>(null)
  const [date, setDate] = useState<Date>(new Date())
  const [user] = useAtom(userAtom)
  const [isLogin] = useAtom(isLoginAtom)

  const handleCalendar = () => {
    setCalendar(!calendar)
  }

  const { isLoading, isError, data, isFetchingNextPage, hasNextPage, fetchNextPage } =
    useInfiniteQuery({
      queryKey: ['history', calendar, date, selectDate],
      queryFn: ({ pageParam }) => {
        if (calendar && selectDate) {
          return getHistoryList(
            user.memberId,
            pageParam,
            getYear(selectDate),
            getMonth(selectDate) + 1,
            getDate(selectDate)
          )
        }
        if (calendar) {
          return getHistoryList(user.memberId, pageParam, getYear(date), getMonth(date) + 1)
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
          fetchNextPage()
        }
      })

      if (history) {
        intObserver.current.observe(history)
      }
    },
    [isFetchingNextPage, fetchNextPage, hasNextPage]
  )

  if (isLoading) return <HistoryListLoading />
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
    <>
      <HomeHeader isLogin={isLogin} userInfo={user} />
      <div className={styles.container}>
        <Toggle handleCalendar={handleCalendar} calendar={calendar} />
        {calendar && (
          <Calendar
            date={date}
            setDate={setDate}
            selectDate={selectDate}
            setSelectDate={setSelectDate}
          />
        )}
        <ul className={styles.historyList}>{body}</ul>
        {isFetchingNextPage && <HistoryLoading />}
      </div>
    </>
  )
}
