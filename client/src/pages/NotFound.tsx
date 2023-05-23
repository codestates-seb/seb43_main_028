import { Link } from 'react-router-dom'
import styles from './NotFound.module.scss'
import useRouter from '../hooks/useRouter'

export default function NotFound() {
  const { routeTo } = useRouter()
  const handleGoBack = () => {
    routeTo(-1)
  }

  return (
    <div className={styles.container}>
      <div className={styles.inner}>
        <p className={styles.title}>페이지를 찾을 수 없습니다</p>
        <p className={styles.content}>
          입력하신 페이지의 주소가 잘못 되었거나,
          <br />
          주소의 변경 혹은 삭제로 인해
          <br />
          현재 페이지를 사용하실 수 없습니다.
          <br />
          주소가 정확한지 다시 한번 확인해 주세요.
        </p>
        <div className={styles.btnBox}>
          <Link className={styles.link} to='/'>
            홈으로
          </Link>
          <button onClick={handleGoBack} type='button' className={styles.goBackBtn}>
            이전 페이지
          </button>
        </div>
      </div>
    </div>
  )
}
