import { useEffect } from 'react'
import styles from './TermsOfUse.module.scss'

type TermsOfUseProps = {
  allCheck: boolean
  setAllCheck: React.Dispatch<React.SetStateAction<boolean>>
  useCheck: boolean
  setUseCheck: React.Dispatch<React.SetStateAction<boolean>>
  privacyCheck: boolean
  setPrivacyCheck: React.Dispatch<React.SetStateAction<boolean>>
}

function TermsOfUse({
  allCheck,
  setAllCheck,
  useCheck,
  setUseCheck,
  privacyCheck,
  setPrivacyCheck,
}: TermsOfUseProps) {
  const allBtnEvent = () => {
    if (allCheck === false) {
      setAllCheck(true)
      setUseCheck(true)
      setPrivacyCheck(true)
    } else {
      setAllCheck(false)
      setUseCheck(false)
      setPrivacyCheck(false)
    }
  }

  const useBtnEvent = () => {
    if (useCheck === false) {
      setUseCheck(true)
    } else {
      setUseCheck(false)
    }
  }

  const privacyBtnEvent = () => {
    if (privacyCheck === false) {
      setPrivacyCheck(true)
    } else {
      setPrivacyCheck(false)
    }
  }

  useEffect(() => {
    if (useCheck === true && privacyCheck === true) {
      setAllCheck(true)
    } else {
      setAllCheck(false)
    }
  }, [useCheck, privacyCheck, setAllCheck])

  return (
    <form method='post' action='' className={styles.form}>
      <div className={styles.form_agreement}>
        <div className={styles.form_agreement_title}>약관동의</div>
        <div className={styles.form_agreement_box}>
          <div className={styles.form_agreement_all}>
            <input type='checkbox' id='all-check' checked={allCheck} onChange={allBtnEvent} />
            <label htmlFor='all-check'>전체동의</label>
          </div>
          <div className={styles.form_agreement_item}>
            <input type='checkbox' id='check2' checked={useCheck} onChange={useBtnEvent} />
            <label htmlFor='check2'>
              이용약관 <span className={styles.blue}>(필수)</span>
            </label>
          </div>
          <div className={styles.form_agreement_item}>
            <input type='checkbox' id='check3' checked={privacyCheck} onChange={privacyBtnEvent} />
            <label htmlFor='check3'>
              개인정보 이용 및 수집에 동의합니다. <span className={styles.gray}>(필수)</span>
            </label>
          </div>
        </div>
      </div>
    </form>
  )
}

export default TermsOfUse
