import styles from './SnapItem.module.scss'
import Icon from '../Icon'
import { timerFormat } from '../../../utils/date'

interface SnapItemProps {
  walkLogContentId: number
  imageUrl: string | null
  seconds: number
  content: string
}

export default function SnapItem({ walkLogContentId, imageUrl, seconds, content }: SnapItemProps) {
  return (
    <div className={styles.container}>
      <div className={styles.imgWrapper}>
        {imageUrl ? <img src={imageUrl} alt='img' /> : <Icon name='no-image' />}
      </div>
      <div className={styles.infoBox}>
        <div className={styles.createdTime}>
          <Icon name='time-gray' size={16} /> {timerFormat(seconds)}
        </div>
        <div className={styles.content}>{content}</div>
        <div className={styles.buttonsWrapper}>
          <button type='button' onClick={() => console.log('수정하기')}>
            <Icon name='edit-gray' size={16} />
            수정
          </button>
          <button type='button' onClick={() => console.log('삭제하기')}>
            <Icon name='trash-gray' size={16} />
            삭제
          </button>
        </div>
      </div>
    </div>
  )
}
