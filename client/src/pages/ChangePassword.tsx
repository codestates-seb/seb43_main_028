import { useState } from 'react'
import { useAtom } from 'jotai'
import { userAtom, idAtom } from '../store/authAtom'
import { signIn } from '../apis/user'
import styles from './ChangePassword.module.scss'
import ChangingPassword from '../components/ChangePassword/ChangingPassword'
import PasswordChanged from '../components/ChangePassword/PasswordChanged'
import Header from '../components/common/Header'

export default function ChangePassword() {
  const [isChangingPassword, setIsChangingPassword] = useState(false)
  const [isPasswordChanged, setIsPasswordChanged] = useState(false)
  const [userId, setUserId] = useState(-1)

  const [user] = useAtom(userAtom)
  const [currentMemberId] = useAtom(idAtom)
  const { email } = user

  const confirmPassword = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    const formData = new FormData(event.currentTarget)
    const password = formData.get('password')
    const { memberId } = await signIn({ email, password, autoLogin: true })

    if (memberId === currentMemberId) {
      setUserId(memberId)
      setIsChangingPassword(true)
    } else {
      alert('잘못된 비밀번호입니다.')
    }
  }

  if (isChangingPassword && !isPasswordChanged) {
    return (
      <>
        <Header
          hasBackButton
          hasCloseButton={false}
          headerTitle='비밀번호 변경'
          handleCloseFn={() => {}}
        />
        <ChangingPassword
          memberId={userId}
          setIsChangingPassword={setIsChangingPassword}
          setIsPasswordChanged={setIsPasswordChanged}
        />
      </>
    )
  }

  if (!isChangingPassword && isPasswordChanged) {
    return (
      <>
        <Header
          hasBackButton
          hasCloseButton={false}
          headerTitle='비밀번호 변경'
          handleCloseFn={() => {}}
        />
        <PasswordChanged
          setIsChangingPassword={setIsChangingPassword}
          setIsPasswordChanged={setIsPasswordChanged}
        />
      </>
    )
  }

  return (
    <>
      <Header
        hasBackButton
        hasCloseButton={false}
        headerTitle='비밀번호 변경'
        handleCloseFn={() => {}}
      />
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
    </>
  )
}
