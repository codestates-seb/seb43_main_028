import { useState } from 'react'
import Icon from '../common/Icon'
import styles from './DetailItem.module.scss'
import ImgModal from './ImgModal'
import { WalkLogContentsDataType } from '../../types/HistoryDetail'

type DetailItemProps = {
  data: WalkLogContentsDataType
  snapTime: string
  onEdit: () => void
  setEditId: React.Dispatch<React.SetStateAction<string | undefined>>
}

export default function DetailItem({ data, snapTime, onEdit, setEditId }: DetailItemProps) {
  const { walkLogContentId, imageUrl, text } = data
  const [imgModal, setImgModal] = useState(false)

  const handleEdit = () => {
    if (walkLogContentId) {
      setEditId(walkLogContentId)
    }
    onEdit()
  }

  const handlePhotoModal = () => {
    setImgModal(prev => !prev)
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
          <div className={styles.editDeleteBox}>
            <button type='button' className={styles.icon} onClick={handleEdit}>
              <Icon name='edit-gray' size={24} />
              수정
            </button>
            <div className={styles.icon}>
              <Icon name='trash-gray' size={24} />
              삭제
            </div>
          </div>
        </div>
      </div>
    </article>
  )
}
