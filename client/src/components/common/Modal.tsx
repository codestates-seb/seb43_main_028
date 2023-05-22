import { useEffect } from 'react'
import styles from './Modal.module.scss'
import Icon from './Icon'

type OptionType = {
  label: string
  handleClick: () => void
  id: number
}

type ModalProps = {
  modalData: {
    title: string
    options: OptionType[]
  }
  onClose: () => void
  style?: React.CSSProperties
}

function Modal({ modalData, onClose, style }: ModalProps) {
  const { title, options } = modalData

  useEffect(() => {
    document.body.style.overflow = 'hidden'
    return () => {
      document.body.style.overflow = 'unset'
    }
  })

  return (
    <div className={styles.modalContainer} style={style}>
      <div role='presentation' className={styles.modalBackground} onClick={onClose} />
      <div className={styles.modal}>
        <div className={styles.modalTop}>
          <div>{title}</div>
          <button type='button' onClick={onClose}>
            <Icon name='close' />
          </button>
        </div>
        <ul className={styles.optionsContainer}>
          {options.map(({ id, label, handleClick }) => {
            return (
              <li key={id}>
                <button type='button' onClick={handleClick}>
                  {label}
                </button>
              </li>
            )
          })}
        </ul>
      </div>
    </div>
  )
}

export default Modal
