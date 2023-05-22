import styles from './Header.module.scss'
import Icon from './Icon'
import useRouter from '../../hooks/useRouter'

type HeaderPropsType = {
  headerTitle: string
  hasBackButton: boolean
  hasCloseButton: boolean
  handleCloseFn: React.Dispatch<React.SetStateAction<boolean>>
  path: string
}

export default function Header({
  headerTitle,
  hasBackButton,
  hasCloseButton,
  handleCloseFn,
  path,
}: HeaderPropsType) {
  const { routeTo } = useRouter()
  const toPreviousPage = () => {
    routeTo(path)
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
        <button type='button' className={styles.closeBtn} onClick={() => handleCloseFn(false)}>
          닫기
        </button>
      ) : (
        <div className={styles.noCloseBtn} />
      )}
    </div>
  )
}
