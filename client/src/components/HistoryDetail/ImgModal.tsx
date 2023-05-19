import styles from './ImgModal.module.scss'

type ImgModalProps = {
  imageUrl: string
  onClose: () => void
}
export default function ImgModal({ imageUrl, onClose }: ImgModalProps) {
  return (
    <>
      <div role='presentation' className={styles.modalBackground} onClick={onClose} />
      <div className={styles.container}>
        <img src={imageUrl} alt='원본 사진' role='presentation' onClick={onClose} />
      </div>
    </>
  )
}
