import Icon from '../common/Icon'
import styles from './SnapItem.module.scss'
import { WalkLogContentsDataType } from '../../types/History'
import { format, ko } from '../../utils/date-fns'

type SnapItemProps = {
  item: WalkLogContentsDataType
}

export default function SnapItem({ item }: SnapItemProps) {
  const { walkLogContentId, imageUrl, text, createdAt } = item

  return (
    <li key={walkLogContentId} className={styles.container}>
      {imageUrl && (
        <div className={styles.imgWrapper}>
          <img src={imageUrl} alt='올린 사진' />
        </div>
      )}
      <div className={imageUrl ? styles.timeTextBox : styles.noImg}>
        <div className={styles.snapTimeBox}>
          <Icon name='time-gray' size={16} />
          <p> {format(new Date(createdAt), 'a h시 m분', { locale: ko })}</p>
        </div>
        <p className={styles.text}>{text}</p>
      </div>
    </li>
  )
}
