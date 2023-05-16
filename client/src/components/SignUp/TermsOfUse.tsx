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
    <form method='post' action='' className={styles.formContainer}>
      <div className={styles.formAgreement}>
        <div className={styles.formAgreementTitle}>약관 동의</div>
        <div className={styles.formAgreementBox}>
          <div className={styles.formAgreementAll}>
            <div className={styles.checkboxWrapper}>
              <input
                className={styles.checkbox}
                type='checkbox'
                id='all-check'
                checked={allCheck}
                onChange={allBtnEvent}
              />
            </div>
            <label htmlFor='all-check'>전체 동의합니다.</label>
          </div>
          <hr />
          <div className={styles.formAgreementItemBox}>
            <div className={styles.formAgreementItem}>
              <div className={styles.checkboxWrapper}>
                <input
                  className={styles.checkbox}
                  type='checkbox'
                  id='check2'
                  checked={useCheck}
                  onChange={useBtnEvent}
                />
              </div>
              <label htmlFor='check2'>
                이용약관에 동의합니다. <span className={styles.blue}>(필수)</span>
              </label>
            </div>
            <div className={styles.formAgreementItem}>
              <div className={styles.checkboxWrapper}>
                <input
                  className={styles.checkbox}
                  type='checkbox'
                  id='check3'
                  checked={privacyCheck}
                  onChange={privacyBtnEvent}
                />
              </div>
              <label htmlFor='check3'>
                개인정보 수집 및 이용에 동의합니다. <span className={styles.gray}>(필수)</span>
              </label>
            </div>
          </div>
        </div>
      </div>
    </form>
  )
}

export default TermsOfUse
