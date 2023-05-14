import { useState } from 'react'
import Icon from '../common/Icon'
import styles from './History.module.scss'
import HistoryItem from './HistoryItem'
import { passedHourMinuteSecondFormat } from '../../utils/date'

interface ItemITF {
  id: number
  createdAt: string
  imageUrl: string
  text: string
}

type HistoryItemProps = {
  data: {
    id: number
    mapImg: string
    startAt: string
    endAt: string
    message: string
    walkLogContents: ItemITF[]
  }
}

export default function History({ data }: HistoryItemProps) {
  const [moreContent, setMore] = useState(false)
  const { mapImg, startAt, endAt, message, walkLogContents } = data
  const date = new Date(startAt)
  const Year = date.getFullYear()
  const Month = date.getMonth() + 1
  const Day = date.getDate()
  const timeDiff = new Date(endAt).getTime() - new Date(startAt).getTime()
  const time = getHourMinuteDiff(timeDiff)
  const moreContents = walkLogContents.slice(1)

  const handleMore = () => {
    setMore(true)
  }

  return (
    <div className={styles.container}>
      <div className={styles.mapTimeBox}>
        <img src={mapImg} className={styles.map} alt='지도 이미지' />
        <div>
          <p className={styles.date}>
            {Year}년 {Month}월 {Day}일
          </p>
          <p className={styles.time}>{time}</p>
        </div>
      </div>
      <p className={styles.message}>{message}</p>
      <HistoryItem item={walkLogContents[0]} startAt={startAt} />
      {!moreContent && (
        <button type='button' className={styles.moreBtn} onClick={handleMore}>
          <Icon name='three-dot' size={24} /> {walkLogContents.length - 1} more
        </button>
      )}
      {moreContent &&
        moreContents.map(item => {
          return <HistoryItem key={item.id} item={item} startAt={startAt} />
        })}
      <button type='button' className={styles.detailBtn}>
        자세히 보기
      </button>
    </div>
  )
}
