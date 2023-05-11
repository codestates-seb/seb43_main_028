import { useState } from 'react'
import Icon from '../common/Icon'
import styles from './History.module.scss'
import HistoryItem from './HistoryItem'

interface ItemITF {
  id: number
  snapTime: string
  imageUrl: string
  text: string
}

type HistoryItemProps = {
  data: {
    id: number
    mapImg: string
    createdAt: string
    time: string
    message: string
    walkLogContents: ItemITF[]
  }
}

export default function History({ data }: HistoryItemProps) {
  const [moreContent, setMore] = useState(false)
  const { mapImg, createdAt, time, message, walkLogContents } = data
  const date = new Date(createdAt)
  const Year = date.getFullYear()
  const Month = date.getMonth()
  const Day = date.getDate()
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
      <HistoryItem item={walkLogContents[0]} />
      {!moreContent && (
        <button type='button' className={styles.moreBtn} onClick={handleMore}>
          <Icon name='three-dot' size={24} /> {walkLogContents.length - 1} more
        </button>
      )}
      {moreContent &&
        moreContents.map(item => {
          return <HistoryItem key={item.id} item={item} />
        })}
      <button type='button' className={styles.detailBtn}>
        자세히 보기
      </button>
    </div>
  )
}
