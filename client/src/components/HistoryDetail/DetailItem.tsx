import Icon from '../common/Icon'
import styles from './DetailItem.module.scss'

type DetailItemProps = {
  data: {
    id: string
    createdAt: string
    imageUrl: string
    text: string
  }
  snapTime: string
  onEdit: () => void
  setEditId: React.Dispatch<React.SetStateAction<string | undefined>>
}

export default function DetailItem({ data, snapTime, onEdit, setEditId }: DetailItemProps) {
  const { id, imageUrl, text } = data

  const handleEdit = () => {
    if (id) {
      setEditId(id)
    }
    onEdit()
  }

  return (
    <article className={styles.contentBox}>
      {imageUrl && <img src={imageUrl} alt='History 사진' />}
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
