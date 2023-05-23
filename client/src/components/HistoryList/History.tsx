import { useState, forwardRef, Ref } from 'react'
import { Link } from 'react-router-dom'
import Icon from '../common/Icon'
import styles from './History.module.scss'
import HistoryItem from './HistoryItem'
import { passedHourMinuteSecondFormat } from '../../utils/date'
import { format } from '../../utils/date-fns'
import { HistoryListDataType } from '../../types/History'

type HistoryItemProps = {
  data: HistoryListDataType
}

export default forwardRef(function History({ data }: HistoryItemProps, ref: Ref<HTMLDivElement>) {
  const [moreContent, setMore] = useState(false)
  const { walkLogId, mapImage, startedAt, endAt, message, walkLogContents } = data
  const timeDiff = new Date(endAt).getTime() - new Date(startedAt).getTime()
  const time = passedHourMinuteSecondFormat(timeDiff)

  const handleMore = () => {
    setMore(true)
  }

  const body = (
    <li className={styles.container}>
      <div className={styles.mapTimeBox}>
        <div className={styles.imgWrapper}>
          <img src={mapImage} className={styles.map} alt='지도 이미지' />
        </div>
        <div>
          <p className={styles.date}>{format(new Date(startedAt), 'yyyy년 M월 d일')}</p>
          <div className={styles.timeBox}>
            <Icon name='time-gray' size={16} />
            <p>{time}</p>
          </div>
        </div>
      </div>
      <p className={styles.message}>{message}</p>
      <ul className={styles.walkLogContentList}>
        {walkLogContents.map((item, index) => {
          if (!moreContent && index > 0) return null
          return <HistoryItem key={item.walkLogContentId} item={item} startAt={startedAt} />
        })}
      </ul>
      {!moreContent && walkLogContents.length > 1 && (
        <button type='button' className={styles.moreBtn} onClick={handleMore}>
          <Icon name='three-dot' size={24} /> {walkLogContents.length - 1} more
        </button>
      )}
      <Link to={`/history/${walkLogId}`} className={styles.detailBtn}>
        자세히 보기
      </Link>
    </li>
  )

  const history = ref ? (
    <div className={styles.containerBox} ref={ref}>
      {body}
    </div>
  ) : (
    <div className={styles.containerBox}>{body}</div>
  )

  return history
})
