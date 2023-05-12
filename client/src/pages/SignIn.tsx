import { Link, useNavigate } from 'react-router-dom'
import { useAtom } from 'jotai'
import { signIn, getCurrentUserInfo } from '../apis/user'
import styles from './SignIn.module.scss'
import userAtom from '../store/userAtom'

function SignIn() {
  const navigate = useNavigate()
  const [, setUserAtom] = useAtom(userAtom)

  const logInSubmitHandler = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    const formData = new FormData(event.currentTarget)

    const email = formData.get('email')
    const password = formData.get('password')

    const memberId = await signIn({ email, password, autoLogin: true })

    if (memberId) {
      const userInfoRes = await getCurrentUserInfo(`/api/members/${memberId}`)
      // 전역 상태에 userInfoRes 저장
      setUserAtom(userInfoRes)
      navigate('/')
      return
    }
    alert('Sorry, you failed to log in.')
  }

  return (
    <div className={styles.container}>
      <form className={styles.form} onSubmit={logInSubmitHandler}>
        <label className={styles.label}>
          Email
          <input className={styles.input} type='email' name='email' required />
        </label>
        <label className={styles.label}>
          Password
          <input className={styles.input} type='password' name='password' required />
        </label>
        <button className={styles.LoginBtn} type='submit'>
          Log in
        </button>
        <span>
          Don’t have an account?<Link to='/signup'>Sign up</Link>
        </span>
      </form>
    </div>
  )
}

export default SignIn
