import styles from './Header.module.scss'
import Icon from './Icon'

type HeaderPropsType = {
  headerTitle: string
  hasBackButton: boolean
  hasCloseButton: boolean
}

export default function Header({ headerTitle, hasBackButton, hasCloseButton }: HeaderPropsType) {
  const toPreviousPage = () => {
    window.history.back()
  }

  return (
    <div className={styles.container}>
      <div className={styles.backBtn}>
        {hasBackButton ? (
          <button className={styles.backBtn} type='button' onClick={toPreviousPage}>
            <Icon name='arrow-left' />
          </button>
        ) : (
          <div className={styles.noBackBtn} />
        )}
      </div>
      <div className={styles.headerTitle}>{headerTitle}</div>
      {hasCloseButton ? (
        <button type='button' className={styles.closeBtn} onClick={toPreviousPage}>
          닫기
        </button>
      ) : (
        <div className={styles.noCloseBtn} />
      )}
    </div>
  )
}
