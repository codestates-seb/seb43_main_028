import { useState } from 'react'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import Icon from '../common/Icon'
import styles from './DetailItem.module.scss'
import ImgModal from './ImgModal'
import { WalkLogContentsDataType } from '../../types/HistoryDetail'
import { deleteHistoryItem } from '../../apis/history'

type DetailItemProps = {
  data: WalkLogContentsDataType
  walkLogId: string
  snapTime: string
  onEdit: () => void
  setEditId: React.Dispatch<React.SetStateAction<string | undefined>>
}

export default function DetailItem({
  data,
  walkLogId,
  snapTime,
  onEdit,
  setEditId,
}: DetailItemProps) {
  const { walkLogContentId, imageUrl, text } = data
  const [imgModal, setImgModal] = useState(false)

  const handleEditMode = () => {
    if (walkLogContentId) {
      setEditId(walkLogContentId)
    }
    onEdit()
  }

  const handlePhotoModal = () => {
    setImgModal(prev => !prev)
  }

  const queryClient = useQueryClient()

  const handleDeleteHistoryItem = useMutation({
    mutationFn: () => deleteHistoryItem(walkLogId, walkLogContentId),
    onSuccess: () => {
      queryClient.invalidateQueries(['history', walkLogId])
    },
  })

  return (
    <article className={styles.contentBox}>
      {imageUrl && (
        <img src={imageUrl} alt='History 사진' onClick={handlePhotoModal} role='presentation' />
      )}
      {imageUrl && imgModal && <ImgModal imageUrl={imageUrl} onClose={handlePhotoModal} />}
      <div className={styles.textBox}>
        {text}
        <div className={styles.iconsBox}>
          <div className={styles.icon}>
            <Icon name='time-gray' size={24} /> {snapTime}
          </div>
          <div className={styles.editDeleteBox}>
            <button type='button' className={styles.icon} onClick={handleEditMode}>
              <Icon name='edit-gray' size={24} />
              수정
            </button>
            <button
              type='button'
              className={styles.icon}
              onClick={() => handleDeleteHistoryItem.mutate()}
              disabled={handleDeleteHistoryItem.isLoading}
            >
              <Icon name='trash-gray' size={24} />
              삭제
            </button>
          </div>
        </div>
      </div>
    </article>
  )
}
