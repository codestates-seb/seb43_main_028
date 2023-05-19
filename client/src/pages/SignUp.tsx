import { useState } from 'react'
import { useForm } from 'react-hook-form'
import Icon from '../components/common/Icon'
import styles from './SignUp.module.scss'
import { signUp } from '../apis/user'
import useRouter from '../hooks/useRouter'
import TermsOfUse from '../components/SignUp/TermsOfUse'

type FormValueType = {
  nickname: string
  email: string
  password: string
}

function SignUp() {
  // 이용약관 관련 state
  const [allCheck, setAllCheck] = useState(false)
  const [useCheck, setUseCheck] = useState(false)
  const [privacyCheck, setPrivacyCheck] = useState(false)

  const emailReg =
    /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i
  const passwordReg = /^(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*])[a-zA-Z\d!@#$%^&*]{10,}$/
  const nicknameReg = /^[가-힣]{2,}$|^[a-zA-Z]{4,}$|^[가-힣a-zA-Z]{4,}$/

  const { routeTo } = useRouter()

  // register: input 요소를 React Hook Form과 연결해 검증 규칙을 적용할 수 있게 하는 메서드
  // formState: form state에 관한 정보를 담고 있는 객체
  // errors: input 값들의 에러 정보를 가지고 있는 객체
  // getValues: input 값을 가져올 수 있는 함수

  const {
    register,
    handleSubmit,
    formState: { dirtyFields, errors },
    getValues,
    // mode
    // onChange: input 값이 바뀔 때마다 검증 로직 동작
    // onBlur: 포커스 상태를 잃을 때 동작
    // onSubmit: submit 함수가 실행될 때 동작
    // onTouched: 첫 번째 blur 이벤트에서 동작하고 그 후에는 모든 change 이벤트에서 동작
    // all: blur 및 change 이벤트에서 동작
  } = useForm<FormValueType>({ mode: 'onChange' })

  const {
    onChange: emailOnChange,
    onBlur: emailOnBlur,
    name: emailName,
    ref: emailRef,
  } = register('email', {
    // required: '이메일을 입력하세요',
    pattern: {
      value: emailReg,
      message: '이메일 형식이 올바르지 않습니다.',
    },
  })

  const {
    onChange: nicknameOnChange,
    onBlur: nicknameOnBlur,
    name: nicknameName,
    ref: nicknameRef,
  } = register('nickname', {
    // required: '닉네임을 입력하세요',
    pattern: {
      value: nicknameReg,
      message: '닉네임은 한글 2글자 이상 또는 영어 4글자 이상이어야 합니다.',
    },
  })

  const {
    onChange: passwordOnChange,
    onBlur: passwordOnBlur,
    name: passwordName,
    ref: passwordRef,
  } = register('password', {
    // required: '비밀번호를 입력하세요',
    minLength: {
      value: 10,
      message: '비밀번호는 총 10자 이상이어야 합니다.',
    },
    maxLength: {
      value: 15,
      message: '비밀번호는 15자 이하여야 합니다.',
    },
    pattern: {
      value: passwordReg,
      message: '영소문자, 숫자, 특수문자가 각각 한 개 이상 포함되어야 합니다.',
    },
  })

  const submitSignUpData = (data: FormValueType) => {
    signUp(data).then(res => {
      if (res === 'success') {
        routeTo('/signin')
      } else if (res === '409-fail') {
        alert('This email has already been registered.')
      } else {
        alert('Sorry, you failed to sign up.')
      }
    })
  }

  return (
    <form
      className={styles.formContainer}
      onSubmit={handleSubmit(data => {
        submitSignUpData(data)
      })}
    >
      <div className={styles.inputBox}>
        <label className={styles.label} htmlFor='email'>
          이메일
        </label>
        <input
          id='email'
          className={styles.input}
          placeholder='이메일을 입력해주세요.'
          onChange={emailOnChange}
          onBlur={emailOnBlur}
          name={emailName}
          ref={emailRef}
        />
        <div className={styles.errorWrapper}>
          {dirtyFields.email && errors.email ? (
            <span className={styles.error}>{errors.email.message}</span>
          ) : (
            <div className={styles.noError} />
          )}
        </div>
      </div>
      <div className={styles.inputBox}>
        <label className={styles.label} htmlFor='password'>
          비밀번호
        </label>
        <input
          id='password'
          placeholder='10~15자리 영대•소문자, 숫자, 특수문자 조합'
          className={styles.input}
          type='password'
          onChange={passwordOnChange}
          onBlur={passwordOnBlur}
          name={passwordName}
          ref={passwordRef}
        />
        <div className={styles.errorWrapper}>
          {dirtyFields.password && errors.password ? (
            <span className={styles.error}>{errors.password.message}</span>
          ) : (
            <div className={styles.noError} />
          )}
        </div>
      </div>
      <div className={styles.inputBox}>
        <label className={styles.label} htmlFor='nickname'>
          이름
        </label>
        <input
          id='nickname'
          placeholder='앱에서 사용할 이름을 입력해주세요.'
          className={styles.input}
          type='text'
          onChange={nicknameOnChange}
          onBlur={nicknameOnBlur}
          name={nicknameName}
          ref={nicknameRef}
        />
        <div className={styles.errorWrapper}>
          {dirtyFields.nickname && errors.nickname ? (
            <span className={styles.error}>{errors.nickname.message}</span>
          ) : (
            <div className={styles.noError} />
          )}
        </div>
      </div>
      <TermsOfUse
        allCheck={allCheck}
        setAllCheck={setAllCheck}
        useCheck={useCheck}
        setUseCheck={setUseCheck}
        privacyCheck={privacyCheck}
        setPrivacyCheck={setPrivacyCheck}
      />
      <button
        className={styles.signupBtn}
        type='submit'
        disabled={!allCheck || !useCheck || !privacyCheck || Object.keys(errors).length > 0}
      >
        회원가입하기
      </button>
    </form>
  )
}

export default SignUp
