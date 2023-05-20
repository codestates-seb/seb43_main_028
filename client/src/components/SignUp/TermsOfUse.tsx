import { useEffect } from 'react'
import styles from './TermsOfUse.module.scss'
import Icon from '../common/Icon'

type TermsOfUseProps = {
  allCheck: boolean
  setAllCheck: React.Dispatch<React.SetStateAction<boolean>>
  termCheck: boolean
  setTermCheck: React.Dispatch<React.SetStateAction<boolean>>
  privacyCheck: boolean
  setPrivacyCheck: React.Dispatch<React.SetStateAction<boolean>>
  setIsTermOfUseOpened: React.Dispatch<React.SetStateAction<boolean>>
  setIsPrivacyPolicyOpened: React.Dispatch<React.SetStateAction<boolean>>
}

function TermsOfUse({
  allCheck,
  setAllCheck,
  termCheck,
  setTermCheck,
  privacyCheck,
  setPrivacyCheck,
  setIsTermOfUseOpened,
  setIsPrivacyPolicyOpened,
}: TermsOfUseProps) {
  const allBtnEvent = () => {
    if (allCheck === false) {
      setAllCheck(true)
      setTermCheck(true)
      setPrivacyCheck(true)
    } else {
      setAllCheck(false)
      setTermCheck(false)
      setPrivacyCheck(false)
    }
  }

  const useBtnEvent = () => {
    if (termCheck === false) {
      setTermCheck(true)
    } else {
      setTermCheck(false)
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
    setAllCheck(false)
    setTermCheck(false)
    setPrivacyCheck(false)
    // eslint-disable-next-line
  }, [])

  useEffect(() => {
    if (termCheck === true && privacyCheck === true) {
      setAllCheck(true)
    } else {
      setAllCheck(false)
    }
  }, [termCheck, privacyCheck, setAllCheck])

  return (
    <div className={styles.container}>
      <div className={styles.agreement}>
        <div className={styles.agreementTitle}>약관 동의</div>
        <div className={styles.agreementBox}>
          <div className={styles.agreementAll}>
            <div className={styles.checkboxWrapper}>
              <button type='button' onClick={allBtnEvent}>
                {allCheck ? <Icon name='after-check' /> : <Icon name='before-check' />}
              </button>
            </div>
            <label htmlFor='all-check'>전체 동의합니다.</label>
          </div>
          <hr />
          <div className={styles.agreementItemBox}>
            <div className={styles.agreementItem}>
              <div className={styles.leftSideElements}>
                <div className={styles.checkboxWrapper}>
                  <button type='button' onClick={useBtnEvent}>
                    {termCheck ? <Icon name='after-check' /> : <Icon name='before-check' />}
                  </button>
                </div>
                <label htmlFor='check2'>
                  이용약관에 동의합니다. <span>(필수)</span>
                </label>
              </div>
              <button
                type='button'
                className={styles.openContentBtn}
                onClick={() => {
                  setIsTermOfUseOpened(true)
                }}
              >
                내용보기
              </button>
            </div>
            <div className={styles.agreementItem}>
              <div className={styles.leftSideElements}>
                <div className={styles.checkboxWrapper}>
                  <button type='button' onClick={privacyBtnEvent}>
                    {privacyCheck ? <Icon name='after-check' /> : <Icon name='before-check' />}
                  </button>
                </div>
                <label htmlFor='check3'>
                  개인정보 수집 및 이용에 동의합니다. <span>(필수)</span>
                </label>
              </div>
              <button
                type='button'
                className={styles.openContentBtn}
                onClick={() => {
                  setIsPrivacyPolicyOpened(true)
                }}
              >
                내용보기
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default TermsOfUse
