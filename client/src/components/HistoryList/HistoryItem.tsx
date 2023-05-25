import Icon from '../common/Icon'
import styles from './HistoryItem.module.scss'
import { WalkLogContentsDataType } from '../../types/History'
import { format, ko } from '../../utils/date-fns'

type HistoryItemProps = {
  item: WalkLogContentsDataType
}

export default function HistoryItem({ item }: HistoryItemProps) {
  const { walkLogContentId, imageUrl, text, createdAt } = item

  return (
    <li key={walkLogContentId} className={styles.container}>
      <div className={styles.imgWrapper}>
        {imageUrl ? <img src={imageUrl} alt='올린 사진' /> : <Icon name='no-image' size={24} />}
      </div>
      <div className={styles.timeTextBox}>
        <div className={styles.snapTimeBox}>
          <Icon name='time-gray' size={16} />
          <p>{format(new Date(createdAt), 'a h시 m분', { locale: ko })}</p>
        </div>
        <p className={styles.text}>{text}</p>
      </div>
    </li>
  )
}
