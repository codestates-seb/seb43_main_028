import { Link } from 'react-router-dom'
import styles from './TempPasswordSent.module.scss'

type TempPasswordSentPropsType = {
  setIsTempPasswordSent: React.Dispatch<React.SetStateAction<boolean>>
}

export default function TempPasswordSent({ setIsTempPasswordSent }: TempPasswordSentPropsType) {
  const handleClickLinkToLogin = () => {
    setIsTempPasswordSent(false)
  }
  return (
    <div className={styles.container}>
      <div className={styles.userEmail}>user@email.com</div>
      <div className={styles.confirmTitle}>임시 비밀번호 발급 완료</div>
      <div className={styles.confirmMessage}>
        <span>임시 비밀번호를 메일로 발송했습니다.</span>
        <span> 로그인 후 비밀번호를 변경해주세요.</span>
      </div>
      <button type='button' className={styles.toLoginPageBtn} onClick={handleClickLinkToLogin}>
        <Link to='/signin'>로그인 하러가기</Link>
      </button>
    </div>
  )
}
