import { useState } from 'react'
import styles from './HistoryList.module.scss'
import History from '../components/HistoryList/History'
import Calendar from '../components/HistoryList/Calendar/Calendar'

interface ItemITF {
  id: number
  createdAt: string
  imageUrl: string
  text: string
}

interface DataITF {
  id: number
  mapImg: string
  startAt: string
  endAt: string
  message: string
  walkLogContents: ItemITF[]
}

export default function HistoryList() {
  const [calendar, setCalendar] = useState<boolean>(false)
  const [data, setData] = useState<DataITF[]>(Data)

  const handleCalendar = () => {
    setCalendar(!calendar)
  }

  return (
    <div className={styles.container}>
      <div className={styles.toggleBox}>
        <button
          type='button'
          className={calendar ? styles.btn : styles.clickedBtn}
          onClick={handleCalendar}
        >
          최신순 보기
        </button>
        <button
          type='button'
          className={calendar ? styles.clickedBtn : styles.btn}
          onClick={handleCalendar}
        >
          월별 보기
        </button>
      </div>
      {calendar && <Calendar data={data} />}
      {Data.map(item => {
        return <History key={item.id} data={item} />
      })}
    </div>
  )
}
