import styles from './HistoryItem.module.scss'

type HistoryItemProps = {
  item: { id: number; snapTime: string; imageUrl: string; text: string }
}

export default function HistoryItem({ item }: HistoryItemProps) {
  const { id, imageUrl, text, snapTime } = item
  return (
    <div key={id} className={styles.container}>
      {imageUrl && <img src={imageUrl} alt='올린 사진' />}
      <div className={styles.timeTextBox}>
        <p>{snapTime}</p>
        <p>{text}</p>
      </div>
    </div>
  )
}
