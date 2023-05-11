import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import styles from './SignUp.module.scss'
import { signUp } from '../apis/user'

function Form() {
  const [displayName, setDisplayName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')

  const navigate = useNavigate()

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    signUp({ displayName, email, password }).then(res => {
      if (res === 'success') {
        navigate('/signin')
      } else if (res === '409-fail') {
        alert('This email has already been registered.')
      } else {
        alert('Sorry, you failed to sign up.')
      }
    })
  }

  return (
    <form className={styles.form} onSubmit={handleSubmit}>
      <label className={styles.label}>
        Display name
        <input
          className={styles.input}
          type='text'
          name='displayName'
          value={displayName}
          onChange={e => setDisplayName(e.target.value)}
          required
        />
      </label>
      <label className={styles.label}>
        Email
        <input
          className={styles.input}
          type='email'
          name='email'
          value={email}
          onChange={e => setEmail(e.target.value)}
          required
        />
      </label>
      <label className={styles.label}>
        Password
        <input
          className={styles.input}
          type='password'
          name='password'
          value={password}
          onChange={e => setPassword(e.target.value)}
          pattern='^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$'
          title='최소 8자, 문자와 숫자를 모두 포함해야 합니다.'
          required
        />
      </label>
      <p className={styles.notice}>
        Passwords must contain at least eight characters,
        <br /> including at least 1 letter and 1 number.
      </p>
      <button className={styles.signupBtn} type='submit'>
        Sign up
      </button>
      <span>
        이미 가입하셨나요? <Link to='/signin'>Log in</Link>
      </span>
    </form>
  )
}

function SignUp() {
  return (
    <div className={styles.entireContainer}>
      <div className={styles.container}>
        <Form />
      </div>
    </div>
  )
}

export default SignUp
