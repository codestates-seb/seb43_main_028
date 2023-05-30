import { useState } from 'react'
import { useForm } from 'react-hook-form'
import styles from './SignUp.module.scss'
import { signUp } from '../apis/user'
import useRouter from '../hooks/useRouter'
import TermsOfUse from '../components/SignUp/TermsOfUse'
import Header from '../components/common/Header'

type FormValueType = {
  nickname: string
  email: string
  password: string
}

function SignUp() {
  // 이용약관 관련 state
  const [allCheck, setAllCheck] = useState(false)
  const [termCheck, setTermCheck] = useState(false)
  const [privacyCheck, setPrivacyCheck] = useState(false)

  const [isTermOfUseOpened, setIsTermOfUseOpened] = useState(false)
  const [isPrivacyPolicyOpened, setIsPrivacyPolicyOpened] = useState(false)

  const emailReg =
    /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/

  const passwordReg = /^(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*])[a-zA-Z\d!@#$%^&*]{10,}$/
  const specialCharacterReg = /[^!@#$%^&*(),.?":{}|<>]/

  const { routeTo } = useRouter()

  // register: input 요소를 React Hook Form과 연결해 검증 규칙을 적용할 수 있게 하는 메서드
  // formState: form state에 관한 정보를 담고 있는 객체
  // errors: input 값들의 에러 정보를 가지고 있는 객체
  // getValues: input 값을 가져올 수 있는 함수

  const {
    register,
    handleSubmit,
    formState: { dirtyFields, errors },
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
    maxLength: {
      value: 16,
      message: '닉네임은 16자 이하여야 합니다.',
    },
    pattern: {
      value: specialCharacterReg,
      message: '닉네임에는 특수문자를 사용할 수 없습니다.',
    },
  })

  const {
    onChange: passwordOnChange,
    onBlur: passwordOnBlur,
    name: passwordName,
    ref: passwordRef,
  } = register('password', {
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
      } else if (res === 'email-exists') {
        alert('이미 가입된 이메일입니다.')
      } else if (res === 'nickname-exists') {
        alert('이미 존재하는 닉네임입니다.')
      } else {
        alert('가입에 실패했습니다. 잠시 후 다시 시도해주세요.')
      }
    })
  }

  if (isTermOfUseOpened) {
    return (
      <>
        <Header
          hasBackButton={false}
          hasCloseButton
          headerTitle='이용약관'
          handleCloseFn={setIsTermOfUseOpened}
          path='-1'
        />
        <div className={styles.termContainer}>
          <strong>걸어볼래 애플리케이션 이용약관 (example)</strong> <br />
          <br />
          <strong>1. 서문</strong> <br />
          걸어볼래 애플리케이션(이하 &quot;앱&quot;이라 함)을 이용해 주셔서 감사합니다. 앱을
          사용하시기 전에 이 이용약관을 주의 깊게 읽어주시기 바랍니다. 앱을 사용함으로써 본 약관에
          동의하는 것으로 간주됩니다. 본 약관은 앱의 이용에 관한 규정을 포함하고 있으며, 앱 이용자와
          운영자인 저희 사이의 권리와 의무를 명확히 하는 것을 목적으로 합니다.
          <br />
          <br />
          2. 앱 서비스 소개 걸어볼래 어플리케이션은 사용자들에게 걷는 즐거움을 제공하는 산책 기록
          서비스입니다. 앱은 GPS를 활용하여 사용자의 이동 경로를 자동으로 기록하고, 사용자는 걷는
          동안 촬영한 사진과 함께 텍스트를 업로드할 수 있습니다. 이를 통해 사용자는 자신의 산책
          일지를 작성하고 기록을 공유할 수 있습니다. <br />
          <br />
          <strong>3. 앱 이용 약관</strong> <br />
          3.1 사용자 계정 앱의 기능을 이용하기 위해 사용자는 계정을 생성해야 합니다. 사용자는
          개인정보를 정확하게 제공해야 하며, 계정 정보는 사용자 개인의 책임 하에 안전하게 관리되어야
          합니다. 계정 정보를 타인에게 노출시키거나 공유해서는 안 됩니다. 앱 운영자는 사용자 계정에
          대한 책임을 지지 않습니다. <br />
          <br />
          3.2 앱 이용 규칙 앱을 이용하는 모든 사용자는 다음 규칙을 준수해야 합니다: <br />
          * 타인의 개인정보를 도용하거나 부정 사용해서는 안 됩니다. <br />
          * 앱 내에서 불법적인 활동을 수행해서는 안됩니다. <br />
          * 타인에 대한 협박, 욕설, 차별, 어포, 명예 훼손 등 악성 행위를 해서는 안 됩니다. <br />
          * 앱의 기능을 악용하여 시스템에 피해를 줄 수 있는 행위를 해서는 안 됩니다.
          <br />
          <br />
          <strong>3.3 개인정보 처리</strong> <br />
          앱은 사용자의 개인정보를 적절하게 보호하기 위해 최선을 다합니다. 개인정보 처리에 대한
          상세한 내용은 앱의 개인정보 처리방침을 참조해 주세요. <br />
          <br />
          4. 책임 제한 앱은 사용자가 앱을 사용함으로써 발생하는 어떠한 손해에 대해서도 책임을 지지
          않습니다. 사용자는 앱을 자유롭게 이용하며, 앱 사용으로 인한 어떠한 결과에 대해서도
          개인적인 책임을 져야 합니다. 앱은 기록된 데이터의 정확성, 완전성, 신뢰성에 대해서는
          보장하지 않습니다. 또한, 앱은 사용자가 업로드한 사진 및 텍스트의 내용에 대해서도 책임을
          지지 않습니다. 사용자는 앱을 안전하고 적법하게 이용하여야 하며, 타인의 권리를 침해하거나
          불법적인 행위를 하여서는 안 됩니다. <br />
          <br />
          5. 서비스 변경 및 중지 앱 운영자는 앱의 일부 또는 전체 서비스를 언제든지 변경, 중지,
          중단할 수 있습니다. 이러한 변경이나 중지로 인해 사용자에게 발생하는 어떠한 손해에 대해서도
          앱 운영자는 책임을 지지 않습니다. <br />
          <br />
          6. 약관의 변경 앱 운영자는 본 약관을 언제든지 수정, 변경할 수 있습니다. 약관의 변경사항은
          앱 내 공지사항을 통해 공지됩니다. 변경사항은 공지된 날로부터 효력이 발생하며, 사용자는
          변경사항을 인지하고 계속적으로 약관을 준수해야 합니다. 본 약관은 2023년 5월 30일부터
          시행됩니다.
        </div>
      </>
    )
  }
  if (isPrivacyPolicyOpened) {
    return (
      <>
        <Header
          hasBackButton={false}
          hasCloseButton
          headerTitle='개인정보 처리방침'
          handleCloseFn={setIsPrivacyPolicyOpened}
          path='-1'
        />
        <div className={styles.termContainer}>
          <strong>걸어볼래 어플리케이션 개인정보 이용 및 수집 (example)</strong>
          <br />
          <br />
          1. 수집하는 개인정보 항목 걸어볼래 어플리케이션은 다음과 같은 개인정보를 수집할 수
          있습니다: <br />
          * 사용자 식별을 위한 이름, 이메일 주소, 프로필 사진 등의 기본 정보 <br />
          * GPS를 통한 이동 경로와 걸은 거리 등의 정보 <br />
          * 사용자가 업로드한 산책 사진과 관련된 텍스트 <br />
          <br />
          <strong>2. 개인정보의 수집 및 이용목적</strong> <br />
          <br />
          걸어볼래 어플리케이션은 다음과 같은 목적으로 개인정보를 수집 및 이용합니다: <br />
          * 사용자 식별과 앱 서비스 이용을 위한 정보 수집 <br />
          * 걷기 기록 정보 제공을 위한 데이터 수집 <br />
          * 산책 사진과 텍스트의 업로드 및 공유를 위한 정보 수집 <br />
          <br />
          <strong>3. 개인정보의 보유 및 이용기간</strong> <br />
          개인정보는 수집 및 이용 목적이 달성된 후에는 지체 없이 파기됩니다. 단, 관련 법령에 의해
          보존이 필요한 경우에는 해당 법령에 따라 일정 기간 동안 보관됩니다. <br />
          <br />
          <strong>
            4. 개인정보의 제3자 제공 <br />
          </strong>
          걸어볼래 어플리케이션은 사용자의 개인정보를 제3자에게 제공하지 않습니다. 단, 사용자가
          명시적으로 동의한 경우 또는 법령에 의거해야 하는 경우에는 예외적으로 개인정보를 제3자에게
          제공할 수 있습니다. <br />
          <br />
          <strong>5. 개인정보의 안전성 보장 </strong>
          걸어볼래 어플리케이션은 개인정보 보호를 위해 보안 조치를 취하고 있습니다. 사용자
          개인정보는 암호화되어 안전하게 저장되며, 외부로부터의 무단 접근 및 유출을 방지하기 위한
          기술적, 물리적 조치를 적용하고 있습니다. <br />
          <br />
          <strong>6. 개인정보에 대한 권리와 관리</strong>
          <br />
          <br />
          사용자는 언제든지 자신의 개인정보에 대한 접근, 수정, 삭제, 이용 제한 요청 등을 할 수
          있습니다. 개인정보 관련 문의 및 요청은 앱 내 설정 메뉴나 고객 지원 채널을 통해 접수하실 수
          있습니다.
        </div>
      </>
    )
  }

  return (
    <>
      <Header
        hasBackButton
        hasCloseButton={false}
        headerTitle='회원가입'
        handleCloseFn={() => {}}
        path='-1'
      />
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
          termCheck={termCheck}
          setTermCheck={setTermCheck}
          privacyCheck={privacyCheck}
          setPrivacyCheck={setPrivacyCheck}
          setIsTermOfUseOpened={setIsTermOfUseOpened}
          setIsPrivacyPolicyOpened={setIsPrivacyPolicyOpened}
        />
        <button
          className={styles.signupBtn}
          type='submit'
          disabled={!allCheck || !termCheck || !privacyCheck || Object.keys(errors).length > 0}
        >
          회원가입하기
        </button>
      </form>
    </>
  )
}

export default SignUp
