import Icon from '../common/Icon'
import styles from './HistoryItem.module.scss'

type HistoryItemProps = {
  item: { id: number; snapTime: string; imageUrl: string; text: string }
}

export default function HistoryItem({ item }: HistoryItemProps) {
  const { id, imageUrl, text, snapTime } = item
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
