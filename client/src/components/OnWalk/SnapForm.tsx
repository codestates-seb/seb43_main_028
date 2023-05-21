import styles from './SnapForm.module.scss'
import ImgInput from './ImgInput'

type HeaderProps = {
  onCancel: (event: React.MouseEvent<HTMLButtonElement>) => void
}

function Header({ onCancel }: HeaderProps) {
  return (
    <div className={styles.header}>
      <h1>순간기록 남기기</h1>
      <div className={styles.btnBox}>
        <button type='button' onClick={onCancel} className={styles.cancelBtn}>
          취소
        </button>
        <button type='submit' className={styles.completeBtn}>
          완료
        </button>
      </div>
    </div>
  )
}

type SnapFormProps = {
  initialImgUrl: string | null
  initialText: string | null
  contentId: string
  handleCancel: () => void
  onSubmit: (contentId: string, formData: FormData) => void
}

export default function SnapForm({
  initialImgUrl,
  initialText,
  contentId,
  handleCancel,
  onSubmit,
}: SnapFormProps) {
  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    const formData = new FormData(event.currentTarget)
    const text = formData.get('text')
    const image: File = formData.get('image')

    const data = new FormData()
    const blob = new Blob([JSON.stringify({ text })], {
      type: 'application/json',
    })

    data.append('content', blob)
    if (image.name) data.append('contentImage', image)
    onSubmit(contentId, data)
  }

  // const handleCancel = () => {
  //   console.log('순간 기록 취소')
  // }

  return (
    <div className={styles.container}>
      <form onSubmit={handleSubmit}>
        <div>
          <Header onCancel={handleCancel} />
          <ImgInput initialValue={initialImgUrl} />
        </div>
        <label htmlFor='text'>
          <textarea
            className={styles.textInput}
            id='text'
            name='text'
            rows={10}
            defaultValue={initialText === null ? '' : initialText}
          />
        </label>
      </form>
    </div>
  )
}
