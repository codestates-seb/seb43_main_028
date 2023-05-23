import { useCallback, useRef, useState } from 'react'
import { useInfiniteQuery } from '@tanstack/react-query'
import { useAtom } from 'jotai'
import { Link } from 'react-router-dom'
import styles from './HistoryList.module.scss'
import History from '../components/HistoryList/History'
import Calendar from '../components/HistoryList/Calendar/Calendar'
import Toggle from '../components/HistoryList/Toggle'
import { getHistoryList } from '../apis/history'
import { userAtom } from '../store/authAtom'
import { HistoryListDataType } from '../types/History'
import { getDate, getMonth, getYear } from '../utils/date-fns'
import HistoryListLoading from './loadingPage/HistoryListLoading'
import HistoryLoading from './loadingPage/HistoryLoading'

export default function HistoryList() {
  const [calendar, setCalendar] = useState<boolean>(false)
  const [selectDate, setSelectDate] = useState<Date | null>(null)
  const [date, setDate] = useState<Date>(new Date())
  const [user] = useAtom(userAtom)

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

  const historyList = data?.pages.map(page => {
    return page.data.map((history: HistoryListDataType, index: number) => {
      if (page.data.length === index + 1) {
        return <History key={history.walkLogId} data={history} ref={lastHistoryRef} />
      }
      return <History key={history.walkLogId} data={history} />
    })
  })

  const noHistoryMessage = (
    <div className={styles.noHistoryBox}>
      <p>
        아직 기록된 순간이 없습니다.
        <br />
        오늘의 기록을 남겨보세요!
      </p>
      <Link to='/' className={styles.homeBtn}>
        홈 화면으로 가기
      </Link>
    </div>
  )

  return (
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
      {data.pages[0].data.length === 0 && noHistoryMessage}
      <ul className={styles.historyList}>{historyList}</ul>
      {isFetchingNextPage && <HistoryLoading />}
    </div>
  )
}
