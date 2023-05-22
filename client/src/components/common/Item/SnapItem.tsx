import { useState } from 'react'
import styles from './SnapItem.module.scss'
import Icon from '../Icon'
import Modal from '../Modal'
import SnapForm from '../SnapForm'
import { timerFormat } from '../../../utils/date'

interface SnapItemProps {
  snapId: number
  imageUrl: string | null
  seconds: number
  content: string
  handleEdit: (snapId: number, data: FormData) => void
  handleDelete: (snapId: number) => void
}

export default function SnapItem({
  snapId,
  imageUrl,
  seconds,
  content,
  handleEdit,
  handleDelete,
}: SnapItemProps) {
  const [isDeleteSnapModalOpen, setIsDeleteSnapModalOpen] = useState(false)
  const [isEditSnapFormOpen, setIsEditSnapFormOpen] = useState(false)

  const deleteSnapModalData = {
    title: '순간기록을 삭제하시겠어요?',
    options: [
      { id: 1, label: '순간기록 삭제', handleClick: () => handleDelete(snapId) },
      { id: 2, label: '취소', handleClick: () => setIsDeleteSnapModalOpen(false) },
    ],
  }

  const editHandler = (data: FormData) => {
    handleEdit(snapId, data)
    setIsEditSnapFormOpen(false)
  }

  const handleEditClick = () => {
    setIsEditSnapFormOpen(true)
  }
  const handleDeleteClick = () => {
    setIsDeleteSnapModalOpen(true)
  }

  return (
    <>
      {isDeleteSnapModalOpen && (
        <Modal
          modalData={deleteSnapModalData}
          onClose={() => setIsDeleteSnapModalOpen(false)}
          style={{ marginLeft: '-40px' }}
        />
      )}

      {isEditSnapFormOpen && (
        <SnapForm
          titleSuffix='수정하기'
          initialImgUrl={imageUrl}
          initialText={content}
          handleCancel={() => setIsEditSnapFormOpen(false)}
          handleSubmit={editHandler}
          style={{ marginLeft: '-40px' }}
        />
      )}

      <div className={styles.container}>
        <div className={styles.imgWrapper}>
          {imageUrl ? <img src={imageUrl} alt='img' /> : <Icon name='no-image' />}
        </div>
        <div className={styles.infoBox}>
          <div className={styles.createdTime}>
            <Icon name='time-gray' size={16} /> {timerFormat(seconds)}
          </div>
          <div className={styles.content}>{content}</div>
          <div className={styles.buttonsWrapper}>
            <button type='button' onClick={handleEditClick}>
              <Icon name='edit-gray' size={16} />
              수정
            </button>
            <button type='button' onClick={handleDeleteClick}>
              <Icon name='trash-gray' size={16} />
              삭제
            </button>
          </div>
        </div>
      </div>
    </>
  )
}
