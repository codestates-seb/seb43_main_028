import styles from './Modal.module.scss'

type Option = {
  label: string
  handleClick: () => void
  id: number
}

type Props = {
  modalData: {
    title: string
    options: Option[]
  }
  onClose: () => void
}

function Modal({ modalData, onClose }: Props): JSX.Element {
  const handleClose = (
    event: React.MouseEvent<HTMLDivElement> | React.MouseEvent<HTMLButtonElement>
  ) => {
    // event.target -> 실제로 클릭된 요소
    // event.currentTarget -> 이벤트가 바인딩된 요소
    // 백그라운드를 클릭했을 때, 그 클릭이 모달 자체를 클릭한 것인지 확인
    if (event.target === event.currentTarget) {
      onClose()
    }
  }

  return (
    <>
      <div role='presentation' className={styles.modalBackground} onClick={handleClose} />
      <div className={styles.modal}>
        <div className={styles.modalTop}>
          <div>{modalData.title}</div>
          <button className={styles.xBtn} type='button' onClick={handleClose}>
            𝖷
          </button>
        </div>
        <ul className={styles.optionsContainer}>
          {modalData.options.map(option => {
            return (
              <li key={option.id}>
                <button type='button' onClick={option.handleClick}>
                  {option.label}
                </button>
              </li>
            )
          })}
        </ul>
      </div>
    </>
  )
}

export default Modal
