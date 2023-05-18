import { useState } from 'react'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { useAtom } from 'jotai'
import Icon from '../common/Icon'
import styles from './DetailItem.module.scss'
import ImgModal from './ImgModal'
import { WalkLogContentsDataType, ModalOption } from '../../types/HistoryDetail'
import { deleteHistoryItem } from '../../apis/history'
import { isLoginAtom, idAtom } from '../../store/authAtom'

type DetailItemProps = {
  data: WalkLogContentsDataType
  walkLogId: string
  memberId: number
  snapTime: string
  onEdit: () => void
  setEditId: React.Dispatch<React.SetStateAction<string | undefined>>
  setOpenDeleteModal: React.Dispatch<React.SetStateAction<boolean>>
  setDeleteModalOption: React.Dispatch<React.SetStateAction<ModalOption>>
}

export default function DetailItem({
  data,
  walkLogId,
  memberId,
  snapTime,
  onEdit,
  setEditId,
  setOpenDeleteModal,
  setDeleteModalOption,
}: DetailItemProps) {
  const { walkLogContentId, imageUrl, text } = data
  const [imgModal, setImgModal] = useState(false)
  const [isLogin] = useAtom(isLoginAtom)
  const [logInId] = useAtom(idAtom)

  const queryClient = useQueryClient()

  const handleDeleteHistoryItem = useMutation({
    mutationFn: () => deleteHistoryItem(walkLogId, walkLogContentId),
    onSuccess: () => {
      queryClient.invalidateQueries(['history', walkLogId])
    },
  })

  const handleEditMode = () => {
    if (walkLogContentId) {
      setEditId(walkLogContentId)
    }
    onEdit()
  }

  const handlePhotoModal = () => {
    setImgModal(prev => !prev)
  }

  const handleDeleteModal = () => {
    setOpenDeleteModal(prev => !prev)
    setDeleteModalOption({
      title: '순간',
      deleteFn: () => {
        handleDeleteHistoryItem.mutate()
        setOpenDeleteModal(prev => !prev)
      },
    })
  }

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
          {isLogin && logInId === memberId && (
            <div className={styles.editDeleteBox}>
              <button type='button' className={styles.icon} onClick={handleEditMode}>
                <Icon name='edit-gray' size={24} />
                수정
              </button>
              <button type='button' className={styles.icon} onClick={handleDeleteModal}>
                <Icon name='trash-gray' size={24} />
                삭제
              </button>
            </div>
          )}
        </div>
      </div>
    </article>
  )
}
