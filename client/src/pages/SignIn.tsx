import { Link } from 'react-router-dom'
import { useAtom } from 'jotai'
import { signIn, getCurrentUserInfo } from '../apis/user'
import styles from './SignIn.module.scss'
import { idAtom, isLoginAtom, userAtom } from '../store/authAtom'
import useRouter from '../hooks/useRouter'

function SignIn() {
  const { routeTo } = useRouter()
  const [, setUser] = useAtom(userAtom)
  const [, setId] = useAtom(idAtom)
  const [, setIsLogin] = useAtom(isLoginAtom)

  const logInSubmitHandler = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    const formData = new FormData(event.currentTarget)

    const email = formData.get('email')
    const password = formData.get('password')

    const memberId = await signIn({ email, password, autoLogin: true })
    setId(memberId)

    if (memberId) {
      const userInfoRes = await getCurrentUserInfo(`/api/members/${memberId}`)
      // 전역 상태에 userInfoRes 저장
      setIsLogin(true)
      setUser(userInfoRes)
      routeTo('/')
      return
    }
    alert('Sorry, you failed to log in.')
  }

  return (
    <form className={styles.formContainer} onSubmit={logInSubmitHandler}>
      <div className={styles.inputBox}>
        <label className={styles.label}>
          이메일
          <input
            className={styles.input}
            placeholder='이메일을 입력해주세요.'
            type='email'
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
            placeholder='비밀번호를 입력해주세요.'
            type='password'
            name='password'
            required
          />
        </label>
      </div>
      <button className={styles.loginBtn} type='submit'>
        로그인하기
      </button>
      <div className={styles.linkWrapper}>
        <span>비밀번호찾기</span>
        <span className={styles.gray}>|</span>
        <Link to='/signup'>
          <span>회원가입하러가기</span>
        </Link>
      </div>
    </form>
  )
}

export default SignIn
