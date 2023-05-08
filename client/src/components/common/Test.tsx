import { useState } from 'react'
import Modal from './Modal'
import styles from './test.module.scss'

function Test() {
  const [isOpen, setIsOpen] = useState(false)

  const handleOpenModal = () => {
    setIsOpen(true)
  }

  const handleCloseModal = () => {
    setIsOpen(false)
  }

  const handleDelete = () => {
    console.log('handle delete')
  }

  const handleKeyDown = (e: React.KeyboardEvent) => {
    console.log(e)
  }

  // 데이터 전달 방식
  const modalData = {
    title: '삭제하시겠습니까?',
    options: [
      { label: 'yes', handleClick: handleDelete, handleKeyDown, id: Math.random() },
      { label: 'no', handleClick: handleCloseModal, handleKeyDown, id: Math.random() },
    ],
  }

  return (
    <div className={styles.container}>
      <button className={styles.btn} type='button' onClick={handleOpenModal}>
        modal lets go
      </button>
      {isOpen && <Modal onClose={handleCloseModal} modalData={modalData} />}
    </div>
  )
}

export default Test
