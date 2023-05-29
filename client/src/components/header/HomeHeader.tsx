import { Link } from 'react-router-dom'
import Icon from '../common/Icon'
import styles from './HomeHeader.module.scss'
import { UserInfoAtomType } from '../../store/authAtom'

type HomeHeaderProps = {
  userInfo: UserInfoAtomType | null
}

export default function HomeHeader({ userInfo }: HomeHeaderProps) {
  return (
    <div className={styles.headerContainer}>
      <div className={styles.profile}>
        {userInfo && userInfo.imageUrl ? (
          <img src={userInfo.imageUrl} alt={userInfo.nickname} />
        ) : (
          <Icon name='no-profile' size={48} />
        )}
      </div>
      <div className={styles.desc}>
        {userInfo ? (
          <>
            <div className={styles.title}>{userInfo.nickname}</div>
            <div className={styles.caption}>
              <span>걷기 {userInfo.totalWalkLog}</span>
              <span>순간기록 {userInfo.totalWalkLogContent}</span>
            </div>
          </>
        ) : (
          <>
            <div className={styles.title}>
              <Link to='/signin'>로그인/회원가입</Link>
            </div>
            <div className={styles.caption}>걸은 순간을 기록으로 간직해보세요.</div>
          </>
        )}
      </div>
    </div>
  )
}
