import { useForm } from 'react-hook-form'
import styles from './ChangingPassword.module.scss'
import { patchUserPassword } from '../../apis/user'

type ChangingPasswordPropsType = {
  memberId: number
  setIsChangingPassword: React.Dispatch<React.SetStateAction<boolean>>
  setIsPasswordChanged: React.Dispatch<React.SetStateAction<boolean>>
}

type FormValueType = {
  newPassword: string
  confirmPassword: string
}

export default function ChangingPassword({
  memberId,
  setIsChangingPassword,
  setIsPasswordChanged,
}: ChangingPasswordPropsType) {
  const newPasswordReg = /^(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*])[a-zA-Z\d!@#$%^&*]{10,}$/

  const {
    register,
    formState: { dirtyFields, errors },
    getValues,
  } = useForm<FormValueType>({ mode: 'onChange' })

  const {
    onChange: newPasswordOnChange,
    onBlur: newPasswordOnBlur,
    name: newPasswordName,
    ref: newPasswordRef,
  } = register('newPassword', {
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
      value: newPasswordReg,
      message: '영소문자, 숫자, 특수문자가 각각 한 개 이상 포함되어야 합니다.',
    },
  })

  const {
    onChange: confirmPasswordOnChange,
    onBlur: confirmPasswordOnBlur,
    name: confirmPasswordName,
    ref: confirmPasswordRef,
  } = register('confirmPassword', {
    validate: value => value === getValues('newPassword') || '비밀번호가 다릅니다.',
  })

  const handleChangePassword = async (event: React.FormEvent) => {
    event.preventDefault()
    const newPassword = getValues('newPassword')
    const passwordData = {
      password: newPassword,
    }
    const res = await patchUserPassword(memberId, passwordData)
    if (res === 'success') {
      setIsChangingPassword(false)
      setIsPasswordChanged(true)
    }
  }

  return (
    <form className={styles.formContainer} onSubmit={handleChangePassword}>
      <div className={styles.inputBox}>
        <label className={styles.label} htmlFor='password'>
          신규 비밀번호
        </label>
        <input
          id='newPassword'
          placeholder='10~15자리 영대•소문자, 숫자, 특수문자 조합'
          className={styles.input}
          type='password'
          onChange={newPasswordOnChange}
          onBlur={newPasswordOnBlur}
          name={newPasswordName}
          ref={newPasswordRef}
        />
        <div className={styles.errorWrapper}>
          {dirtyFields.newPassword && errors.newPassword ? (
            <span className={styles.error}>{errors.newPassword.message}</span>
          ) : (
            <div className={styles.noError} />
          )}
        </div>
      </div>
      <div className={styles.inputBox}>
        <label className={styles.label} htmlFor='password'>
          신규 비밀번호 확인
        </label>
        <input
          id='password'
          placeholder='비밀번호를 확인해주세요.'
          className={styles.input}
          type='password'
          onChange={confirmPasswordOnChange}
          onBlur={confirmPasswordOnBlur}
          name={confirmPasswordName}
          ref={confirmPasswordRef}
        />
        <div className={styles.errorWrapper}>
          {dirtyFields.confirmPassword && errors.confirmPassword ? (
            <span className={styles.error}>{errors.confirmPassword.message}</span>
          ) : (
            <div className={styles.noError} />
          )}
        </div>
      </div>
      <button
        className={styles.changePasswordBtn}
        type='submit'
        disabled={Object.keys(errors).length > 0}
      >
        비밀번호 변경하기
      </button>
    </form>
  )
}
