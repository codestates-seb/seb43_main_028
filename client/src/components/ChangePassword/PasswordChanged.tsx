import { Link } from 'react-router-dom'
import { useAtom } from 'jotai'
import { isLoginAtom } from '../../store/authAtom'
import styles from './PasswordChanged.module.scss'

export default function PasswordChanged() {
  const [isLogin, setIsLogin] = useAtom(isLoginAtom)
  const handleLogout = () => {
    console.log('handleLogout')
    setIsLogin(false)
  }
  return (
    <div className={styles.container}>
      <div className={styles.confirmTitle}>비밀번호 변경완료</div>
      <div className={styles.confirmMessage}>변경하신 비밀번호로 다시 로그인해주세요.</div>
      <button type='button' className={styles.toLoginPageBtn} onClick={handleLogout}>
        <Link to='/signin'>로그인 하러가기</Link>
      </button>
    </div>
  )
}
