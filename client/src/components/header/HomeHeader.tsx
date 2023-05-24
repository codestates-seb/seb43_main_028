import { Link } from 'react-router-dom'
import Icon from '../common/Icon'
import styles from './HomeHeader.module.scss'

type HomeHeaderProps = {
  isLogin: boolean
  userInfo: {
    nickname: string
    imageUrl: string
    totalWalkLog: number
    totalWalkLogContent: number
  }
}

export default function HomeHeader({ isLogin, userInfo }: HomeHeaderProps) {
  const { nickname, imageUrl, totalWalkLog, totalWalkLogContent } = userInfo

  return (
    <div className={styles.headerContainer}>
      <div className={styles.profile}>
        {isLogin && imageUrl ? (
          <img src={imageUrl} alt={nickname} />
        ) : (
          <Icon name='no-profile' size={48} />
        )}
      </div>
      <div className={styles.desc}>
        {isLogin ? (
          <>
            <div className={styles.title}>{nickname}</div>
            <div className={styles.caption}>
              <span>걷기 {totalWalkLog}</span>
              <span>순간기록 {totalWalkLogContent}</span>
            </div>
          </>
        ) : (
          <>
            <div className={styles.title}>
              <Link to='/signin'>로그인/회원가입</Link>
            </div>
            <div className={styles.caption}>걸은 순간을 기록으로 간직해보세요.</div>
            {/* <div className={styles.caption}>로그인 없이 걷기를 체험할 수 있습니다.</div> */}
          </>
        )}
      </div>
    </div>
  )
}
