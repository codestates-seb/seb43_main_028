import { useState } from 'react'
import { useForm } from 'react-hook-form'
import styles from './FindPassword.module.scss'
import TempPasswordSent from '../components/FindPassword/TempPasswordSent'

type FormValueType = {
  email: string
}

export default function FindPassword() {
  const [isTempPasswordSent, setIsTempPasswordSent] = useState(false)
  const {
    register,
    handleSubmit,
    formState: { dirtyFields, errors },
    getValues,
  } = useForm<FormValueType>({ mode: 'onChange' })

  const emailReg =
    /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/

  const {
    onChange: emailOnChange,
    onBlur: emailOnBlur,
    name: emailName,
    ref: emailRef,
  } = register('email', {
    pattern: {
      value: emailReg,
      message: '이메일 형식이 올바르지 않습니다.',
    },
  })

  const handleFormSubmit = (data: FormValueType, event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault() // 기본 동작 막기
    handleGetTempPassword(data) // 임시 비밀번호 발급 처리 등의 로직 수행
  }
  const handleGetTempPassword = (data: FormValueType) => {
    console.log(data)
  }

  if (isTempPasswordSent) {
    return <TempPasswordSent setIsTempPasswordSent={setIsTempPasswordSent} />
  }
  return (
    <form
      className={styles.formContainer}
      onSubmit={handleSubmit((data, event) => {
        handleFormSubmit(data, event as React.FormEvent<HTMLFormElement>)
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
      <button
        className={styles.tempPwGetBtn}
        type='submit'
        disabled={!getValues('email') || Object.keys(errors).length > 0}
      >
        임시 비밀번호 발급 받기
      </button>
    </form>
  )
}
