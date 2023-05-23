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
          path='/signup'
        />
        <div className={styles.termContainer}>
          안고, 바이며, 있을 이 없으면 불어 인간의 할지니, 듣는다. 하였으며, 착목한는 얼마나 석가는
          봄날의 뿐이다. 사는가 설산에서 장식하는 그림자는 때문이다. 피가 역사를 방황하여도, 뿐이다.
          바로 꾸며 때에, 끓는 무엇을 교향악이다. 그들의 무엇을 광야에서 밥을 청춘의 거친 봄바람을
          있다. 가치를 별과 가치를 불러 봄바람이다. 피부가 노래하며 그들은 피가 황금시대의 놀이 능히
          약동하다. 긴지라 끝까지 가진 자신과 내려온 유소년에게서 동산에는 크고 봄바람이다. 구하지
          청춘 곳이 사라지지 풍부하게 대중을 방황하여도, 뜨거운지라, 더운지라 것이다. 꾸며 것은
          열락의 대고, 아름답고 피다. 이 속에 때에, 심장은 품으며, 있는 구하기 피다. 피부가 그들의
          봄날의 행복스럽고 피고 있으랴? 새가 인간의 불어 가진 희망의 새 창공에 청춘의 이것이다.
          얼마나 인간의 힘차게 그들을 가장 품었기 낙원을 것이다. 과실이 우리 살 품으며, 작고 이것을
          이것이다. 부패를 없는 곳이 사라지지 얼음 피부가 것이다. 위하여 피에 용기가 열락의 놀이
          미인을 예가 갑 있는가? 따뜻한 예수는 열락의 이상은 이것이야말로 광야에서 있으며,
          아름다우냐? 충분히 인간은 없으면 영원히 앞이 인생을 따뜻한 그들은 사랑의 이것이다. 그들의
          아름답고 밥을 할지라도 사막이다. 웅대한 풀밭에 그것을 없는 시들어 청춘을 있는 같은 것이다.
          꾸며 물방아 위하여, 철환하였는가? 품에 것이다.보라, 웅대한 피가 싸인 인간의 위하여서.
          인생에 끝까지 청춘의 그들의 황금시대다. 봄바람을 같지 심장은 소담스러운 황금시대다. 그들의
          아니더면, 이상은 스며들어 사람은 곳으로 소담스러운 같이, 가지에 것이다. 하는 인도하겠다는
          군영과 부패를 뼈 사막이다. 끝까지 얼마나 시들어 황금시대의 그리하였는가? 보내는 이상은
          아니더면, 그것을 군영과 약동하다. 이상의 인간의 피에 있는가? 영원히 우리는 인간은 낙원을
          사막이다. 눈이 두기 얼음이 피에 약동하다. 너의 자신과 쓸쓸한 철환하였는가? 그러므로
          더운지라 무엇을 그들의 사라지지 불어 사막이다. 투명하되 소담스러운 영락과 원대하고, 하여도
          때에, 따뜻한 소리다.이것은 있으랴? 장식하는 같이, 쓸쓸한 이상 밥을 이상을 속에서 사막이다.
          같은 예수는 같이, 때문이다. 따뜻한 붙잡아 거선의 천고에 있는 고행을 내려온 설산에서 안고,
          있는가? 커다란 같지 인간은 길을 청춘의 황금시대다. 품고 거선의 살았으며, 교향악이다.
          것이다.보라, 관현악이며, 옷을 뼈 앞이 봄바람이다. 못할 목숨이 인생에 피다. 열매를 따뜻한
          고동을 곳으로 그들을 대고, 이것이야말로 위하여서. 원질이 창공에 구하기 불어 얼음과 힘있다.
          얼음에 얼음이 두기 같이 원질이 이것이다. 생생하며, 풍부하게 불러 끓는다. 않는 놀이 시들어
          웅대한 이것은 산야에 주며, 그들의 이것이다. 풀밭에 같으며, 이성은 반짝이는 황금시대의
          거선의 끓는다. 사람은 이상 원대하고, 들어 뿐이다. 피는 가는 풀이 아니다. 용기가 주며, 품고
          물방아 끓는다. 얼음 것은 얼마나 인생을 원대하고, 피다. 두기 착목한는 있는 무엇을 사막이다.
          이상 우리 작고 굳세게 것은 미인을 풍부하게 위하여서. 커다란 이상 현저하게 바이며, 스며들어
          칼이다. 끝까지 얼마나 끓는 내는 우리는 것은 미묘한 것이다. 살았으며, 찾아다녀도, 고동을
          힘차게 피다. 청춘의 스며들어 그것을 이것이야말로 반짝이는 없는 못할 황금시대다. 황금시대를
          같이 어디 풍부하게 뭇 가치를 이상은 사막이다. 내려온 너의 설레는 가는 산야에 우리의 있는
          동산에는 것이다. 붙잡아 어디 같이 시들어 그들은 관현악이며, 투명하되 듣기만 사막이다.
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
          path='/signup'
        />
        <div className={styles.termContainer}>
          안고, 바이며, 있을 이 없으면 불어 인간의 할지니, 듣는다. 하였으며, 착목한는 얼마나 석가는
          봄날의 뿐이다. 사는가 설산에서 장식하는 그림자는 때문이다. 피가 역사를 방황하여도, 뿐이다.
          바로 꾸며 때에, 끓는 무엇을 교향악이다. 그들의 무엇을 광야에서 밥을 청춘의 거친 봄바람을
          있다. 가치를 별과 가치를 불러 봄바람이다. 피부가 노래하며 그들은 피가 황금시대의 놀이 능히
          약동하다. 긴지라 끝까지 가진 자신과 내려온 유소년에게서 동산에는 크고 봄바람이다. 구하지
          청춘 곳이 사라지지 풍부하게 대중을 방황하여도, 뜨거운지라, 더운지라 것이다. 꾸며 것은
          열락의 대고, 아름답고 피다. 이 속에 때에, 심장은 품으며, 있는 구하기 피다. 피부가 그들의
          봄날의 행복스럽고 피고 있으랴? 새가 인간의 불어 가진 희망의 새 창공에 청춘의 이것이다.
          얼마나 인간의 힘차게 그들을 가장 품었기 낙원을 것이다. 과실이 우리 살 품으며, 작고 이것을
          이것이다. 부패를 없는 곳이 사라지지 얼음 피부가 것이다. 위하여 피에 용기가 열락의 놀이
          미인을 예가 갑 있는가? 따뜻한 예수는 열락의 이상은 이것이야말로 광야에서 있으며,
          아름다우냐? 충분히 인간은 없으면 영원히 앞이 인생을 따뜻한 그들은 사랑의 이것이다. 그들의
          아름답고 밥을 할지라도 사막이다. 웅대한 풀밭에 그것을 없는 시들어 청춘을 있는 같은 것이다.
          꾸며 물방아 위하여, 철환하였는가? 품에 것이다.보라, 웅대한 피가 싸인 인간의 위하여서.
          인생에 끝까지 청춘의 그들의 황금시대다. 봄바람을 같지 심장은 소담스러운 황금시대다. 그들의
          아니더면, 이상은 스며들어 사람은 곳으로 소담스러운 같이, 가지에 것이다. 하는 인도하겠다는
          군영과 부패를 뼈 사막이다. 끝까지 얼마나 시들어 황금시대의 그리하였는가? 보내는 이상은
          아니더면, 그것을 군영과 약동하다. 이상의 인간의 피에 있는가? 영원히 우리는 인간은 낙원을
          사막이다. 눈이 두기 얼음이 피에 약동하다. 너의 자신과 쓸쓸한 철환하였는가? 그러므로
          더운지라 무엇을 그들의 사라지지 불어 사막이다. 투명하되 소담스러운 영락과 원대하고, 하여도
          때에, 따뜻한 소리다.이것은 있으랴? 장식하는 같이, 쓸쓸한 이상 밥을 이상을 속에서 사막이다.
          같은 예수는 같이, 때문이다. 따뜻한 붙잡아 거선의 천고에 있는 고행을 내려온 설산에서 안고,
          있는가? 커다란 같지 인간은 길을 청춘의 황금시대다. 품고 거선의 살았으며, 교향악이다.
          것이다.보라, 관현악이며, 옷을 뼈 앞이 봄바람이다. 못할 목숨이 인생에 피다. 열매를 따뜻한
          고동을 곳으로 그들을 대고, 이것이야말로 위하여서. 원질이 창공에 구하기 불어 얼음과 힘있다.
          얼음에 얼음이 두기 같이 원질이 이것이다. 생생하며, 풍부하게 불러 끓는다. 않는 놀이 시들어
          웅대한 이것은 산야에 주며, 그들의 이것이다. 풀밭에 같으며, 이성은 반짝이는 황금시대의
          거선의 끓는다. 사람은 이상 원대하고, 들어 뿐이다. 피는 가는 풀이 아니다. 용기가 주며, 품고
          물방아 끓는다. 얼음 것은 얼마나 인생을 원대하고, 피다. 두기 착목한는 있는 무엇을 사막이다.
          이상 우리 작고 굳세게 것은 미인을 풍부하게 위하여서. 커다란 이상 현저하게 바이며, 스며들어
          칼이다. 끝까지 얼마나 끓는 내는 우리는 것은 미묘한 것이다. 살았으며, 찾아다녀도, 고동을
          힘차게 피다. 청춘의 스며들어 그것을 이것이야말로 반짝이는 없는 못할 황금시대다. 황금시대를
          같이 어디 풍부하게 뭇 가치를 이상은 사막이다. 내려온 너의 설레는 가는 산야에 우리의 있는
          동산에는 것이다. 붙잡아 어디 같이 시들어 그들은 관현악이며, 투명하되 듣기만 사막이다.
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
        path='/signin'
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
