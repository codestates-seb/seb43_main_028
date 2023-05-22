import Icon from '../common/Icon'
import styles from './SnapItem.module.scss'
import { timerFormat } from '../../utils/date'
import { WalkLogContentsDataType } from '../../types/History'
import { differenceInSeconds } from '../../utils/date-fns'

type SnapItemProps = {
  item: WalkLogContentsDataType
  startAt: string | number | Date
}

export default function SnapItem({ item, startAt }: SnapItemProps) {
  const { walkLogContentId, imageUrl, text, createdAt } = item
  const snapTimeDiff = differenceInSeconds(new Date(createdAt), new Date(startAt))
  const snapTime = timerFormat(snapTimeDiff)

  return (
    <li key={walkLogContentId} className={styles.container}>
      {imageUrl && <img src={imageUrl} alt='올린 사진' />}
      <div className={imageUrl ? styles.timeTextBox : styles.noImg}>
        <div className={styles.snapTimeBox}>
          <Icon name='time-gray' size={16} />
          <p>{snapTime}</p>
        </div>
        <p className={styles.text}>{text}</p>
      </div>
    </li>
  )
}
