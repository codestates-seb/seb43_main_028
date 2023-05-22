import { useAtomValue } from 'jotai'
import { isLoginAtom, userAtom } from '../../store/authAtom'
import { format } from '../../utils/date-fns'
import Icon from '../common/Icon'
import Timer from '../common/Timer'
import styles from './OnWalkHeader.module.scss'

interface OnWalkHeaderProps {
  startedAt: string
  handleFinishClick: () => void
}

export default function OnWalkHeader({ startedAt, handleFinishClick }: OnWalkHeaderProps) {
  const isLogin = useAtomValue(isLoginAtom)
  const userInfo = useAtomValue(userAtom)

  const startedDate = new Date(startedAt)

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
          <p className={styles.text}>
            {isLogin ? `${userInfo.totalWalkLog}번째 걷는 중이에요.` : '걷기 체험 중이에요.'}
          </p>
        </div>
      </div>
      <button className={styles.btn} type='button' onClick={handleFinishClick}>
        <Timer startedDate={startedDate} />
        종료
      </button>
    </div>
  )
}
