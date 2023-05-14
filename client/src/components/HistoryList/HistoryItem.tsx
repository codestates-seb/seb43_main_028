import Icon from '../common/Icon'
import styles from './HistoryItem.module.scss'
import { timerFormat } from '../../utils/date'

type HistoryItemProps = {
  item: { id: number; createdAt: string; imageUrl: string; text: string }
  startAt: string
}

export default function HistoryItem({ item, startAt }: HistoryItemProps) {
  const { id, imageUrl, text, createdAt } = item
  const snapTimeDiff = new Date(createdAt).getTime() - new Date(startAt).getTime()
  const snapTime = timerFormat(snapTimeDiff / 1000)

  return (
    <div key={id} className={styles.container}>
      {imageUrl && <img src={imageUrl} alt='올린 사진' />}
      <div className={imageUrl ? styles.timeTextBox : styles.noImg}>
        <div className={styles.snapTimeBox}>
          <Icon name='time-gray' size={16} />
          <p>{snapTime}</p>
        </div>
        <p className={styles.text}>{text}</p>
      </div>
    </div>
  )
}
