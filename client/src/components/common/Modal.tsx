import { useEffect } from 'react'
import styles from './Modal.module.scss'
import Icon from './Icon'

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

function Modal({ modalData, onClose }: Props) {
  const { title, options } = modalData

  useEffect(() => {
    document.body.style.overflow = 'hidden'
    return () => {
      document.body.style.overflow = 'unset'
    }
  })

  return (
    <>
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
    </>
  )
}

export default Modal
