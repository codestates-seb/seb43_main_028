import { useAtom } from 'jotai'
import { isLoginAtom } from '../../store/authAtom'
import styles from './PasswordChanged.module.scss'
import { logoutUser } from '../../apis/user'
import useRouter from '../../hooks/useRouter'

type PasswordChangedPropsType = {
  setIsChangingPassword: React.Dispatch<React.SetStateAction<boolean>>
  setIsPasswordChanged: React.Dispatch<React.SetStateAction<boolean>>
}

export default function PasswordChanged({
  setIsChangingPassword,
  setIsPasswordChanged,
}: PasswordChangedPropsType) {
  const [, setIsLogin] = useAtom(isLoginAtom)
  const { routeTo } = useRouter()
  const handleLogout = async () => {
    await logoutUser()
    setIsLogin(false)
    setIsChangingPassword(false)
    setIsPasswordChanged(false)
    routeTo('/signin')
  }
  return (
    <div className={styles.container}>
      <div className={styles.confirmTitle}>비밀번호 변경완료</div>
      <div className={styles.confirmMessage}>변경하신 비밀번호로 다시 로그인해주세요.</div>
      <button type='button' className={styles.toLoginPageBtn} onClick={handleLogout}>
        <span>로그인 하러가기</span>
      </button>
    </div>
  )
}
