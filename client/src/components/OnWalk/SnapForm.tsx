import { useState } from 'react'
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
  const [preview, setPreview] = useState<string | null>(initialImgUrl)

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    const formData = new FormData(event.currentTarget)
    const text = formData.get('text') ? formData.get('text') : null
    const image = formData.get('image')

    const data = new FormData()
    const blob = new Blob([JSON.stringify({ text })], {
      type: 'application/json',
    })

    data.append('content', blob)

    if (image instanceof File && !image.name && !preview) {
      data.append('contentImage', image)
    }

    if (image instanceof File && image.name && preview) {
      data.append('contentImage', image)
    }
    onSubmit(contentId, data)
  }

  return (
    <div className={styles.container}>
      <form onSubmit={handleSubmit}>
        <div>
          <Header onCancel={handleCancel} />
          <ImgInput initialImgUrl={initialImgUrl} preview={preview} setPreview={setPreview} />
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
