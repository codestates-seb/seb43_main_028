import { Link } from 'react-router-dom'
import { useSetAtom } from 'jotai'
import { signIn, getCurrentUserInfo } from '../apis/user'
import styles from './SignIn.module.scss'
import { userInfoAtom } from '../store/authAtom'
import useRouter from '../hooks/useRouter'
import Header from '../components/common/Header'

function SignIn() {
  const { routeTo } = useRouter()
  const setUserInfo = useSetAtom(userInfoAtom)

  const logInSubmitHandler = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    const formData = new FormData(event.currentTarget)

    const email = formData.get('email')
    const password = formData.get('password')

    const { status } = await signIn({ email, password, autoLogin: true })

    if (status === 'success') {
      const userInfo = await getCurrentUserInfo()
      setUserInfo(userInfo)
      routeTo('/')
      return
    }
    if (status === 'invalid-info') alert('가입되지 않은 이메일이거나 잘못된 비밀번호입니다.')
    if (status === 'unknown-error') alert('잠시 후 다시 시도해주세요.')
  }

  return (
    <>
      <Header
        hasBackButton
        hasCloseButton={false}
        headerTitle='로그인'
        handleCloseFn={() => {}}
        path='-1'
      />
      <form className={styles.formContainer} onSubmit={logInSubmitHandler}>
        <div className={styles.inputBox}>
          <label className={styles.label}>
            이메일
            <input
              className={styles.input}
              placeholder='이메일을 입력해주세요.'
              name='email'
              required
            />
          </label>
        </div>
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
        <button className={styles.loginBtn} type='submit'>
          로그인하기
        </button>
        <div className={styles.linkWrapper}>
          <Link to='/findpassword'>
            <span>비밀번호찾기</span>
          </Link>
          <span className={styles.gray}>|</span>
          <Link to='/signup'>
            <span>회원가입하러가기</span>
          </Link>
        </div>
      </form>
    </>
  )
}

export default SignIn
