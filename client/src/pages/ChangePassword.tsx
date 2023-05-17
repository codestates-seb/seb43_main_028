import { useState } from 'react'
import { useAtom } from 'jotai'
import { userAtom } from '../store/authAtom'
import { signIn } from '../apis/user'
import styles from './ChangePassword.module.scss'
import ChangingPassword from '../components/ChangePassword/ChangingPassword'
import PasswordChanged from '../components/ChangePassword/PasswordChanged'

export default function ChangePassword() {
  const [isChangingPassword, setIsChangingPassword] = useState(false)
  const [isPasswordChanged, setIsPasswordChanged] = useState(false)

  const [user] = useAtom(userAtom)
  const { email } = user

  const confirmPassword = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    const formData = new FormData(event.currentTarget)
    const password = formData.get('password')
    const memberId = await signIn({ email, password, autoLogin: true })

    if (memberId) {
      setIsChangingPassword(true)
    }
  }

  if (isChangingPassword && !isPasswordChanged) {
    return (
      <ChangingPassword
        email={email}
        setIsChangingPassword={setIsChangingPassword}
        setIsPasswordChanged={setIsPasswordChanged}
      />
    )
  }

  if (!isChangingPassword && isPasswordChanged) {
    return <PasswordChanged />
  }

  return (
    <form className={styles.formContainer} onSubmit={confirmPassword}>
      <div className={styles.inputBox}>
        <label className={styles.label}>
          비밀번호
          <input
            className={styles.input}
            type='password'
            placeholder='비밀번호를 입력해주세요.'
            name='password'
            required
          />
        </label>
      </div>
      <button className={styles.checkPasswordBtn} type='submit'>
        비밀번호 확인하기
      </button>
    </form>
  )
}
