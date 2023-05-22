import { useAtomValue } from 'jotai'
import { isLoginAtom, userAtom } from '../../store/authAtom'
import { format } from '../../utils/date-fns'
import Icon from '../common/Icon'
import Timer from '../common/Timer'
import styles from './WalkHeader.module.scss'

interface WalkHeaderProps {
  type: 'AFTER' | 'ON'
  startedAt: string
  handleFinishClick?: () => void
}

export default function WalkHeader({ type, startedAt, handleFinishClick }: WalkHeaderProps) {
  const isLogin = useAtomValue(isLoginAtom)
  const userInfo = useAtomValue(userAtom)

  const startedDate = new Date(startedAt)

  const getMessage = (type: WalkHeaderProps['type']) => {
    if (type === 'ON') {
      return `${userInfo.totalWalkLog}번째 걷는 중이에요.`
    }
    if (type === 'AFTER') {
      return `${userInfo.nickname}님의 ${userInfo.totalWalkLog}번째 걷기를 완료하셨어요.`
    }
  }

  return (
    <div className={styles.container}>
      <div className={styles.infoBox}>
        <div className={styles.profileBox}>
          {isLogin && userInfo.imageUrl ? (
            <img src={userInfo.imageUrl} alt={userInfo.nickname} />
          ) : (
            <Icon name='no-profile' size={48} />
          )}
        </div>
        <div className={styles.contentBox}>
          <div className={styles.createAt}>
            <span className='date'>{format(startedDate, 'yyyy-MM-dd')}</span>
            <span className='time'>{format(startedDate, 'HH:mm:ss')}</span>
          </div>
          <p className={styles.text}>{getMessage(type)}</p>
        </div>
      </div>
      {type === 'ON' && (
        <button className={styles.btn} type='button' onClick={handleFinishClick}>
          <Timer startedDate={startedDate} />
          종료
        </button>
      )}
    </div>
  )
}
