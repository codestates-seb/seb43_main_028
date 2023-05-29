import { useCallback, useRef, useState } from 'react'
import { useInfiniteQuery } from '@tanstack/react-query'
import { useAtomValue } from 'jotai'
import { Link } from 'react-router-dom'
import styles from './HistoryList.module.scss'
import History from '../components/HistoryList/History'
import Calendar from '../components/HistoryList/Calendar/Calendar'
import Toggle from '../components/HistoryList/Toggle'
import { getHistoryList } from '../apis/history'
import { UserInfoAtomType, userInfoAtom } from '../store/authAtom'
import { HistoryListDataType } from '../types/History'
import { getDate, getMonth, getYear } from '../utils/date-fns'
import HistoryLoading from './loadingPage/HistoryLoading'
import HomeHeader from '../components/header/HomeHeader'

export default function HistoryList() {
  const [calendar, setCalendar] = useState<boolean>(false)
  const [selectDate, setSelectDate] = useState<Date | null>(null)
  const [date, setDate] = useState<Date>(new Date())
  const userInfo = useAtomValue(userInfoAtom) as UserInfoAtomType

  const intObserver = useRef<IntersectionObserver>()

  const { isError, data, isFetchingNextPage, hasNextPage, fetchNextPage } = useInfiniteQuery({
    queryKey: ['history', calendar, date, selectDate],
    queryFn: ({ pageParam }) => {
      if (calendar && selectDate) {
        return getHistoryList(
          userInfo.memberId,
          pageParam,
          getYear(selectDate),
          getMonth(selectDate) + 1,
          getDate(selectDate)
        )
      }
      if (calendar) {
        return getHistoryList(userInfo.memberId, pageParam, getYear(date), getMonth(date) + 1)
      }
      return getHistoryList(userInfo.memberId, pageParam)
    },
    getNextPageParam: lastPage => {
      const { page, totalPages } = lastPage.pageInfo
      return page < totalPages ? page + 1 : undefined
    },
  })

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

  const handleCalendar = () => {
    setCalendar(!calendar)
  }

  if (!userInfo) {
    return (
      <div className={styles.noHistoryBox}>
        <p>
          로그인/회원가입을 하고
          <br />
          오늘의 기록을 남겨보세요!
        </p>
        <Link to='/signin' className={styles.linkBtn}>
          로그인 하러가기
        </Link>
        <Link to='/signup' className={styles.linkBtn}>
          회원가입 하러가기
        </Link>
      </div>
    )
  }

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
    <>
      <HomeHeader userInfo={userInfo} />
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
        {data?.pages[0].data.length === 0 && !calendar && noHistoryMessage}
        <ul className={styles.historyList}>{historyList}</ul>
        {isFetchingNextPage && <HistoryLoading />}
      </div>
    </>
  )
}
