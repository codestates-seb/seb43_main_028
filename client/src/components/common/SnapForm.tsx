import { useEffect, useState } from 'react'
import ImgInput from './ImgInput'
import styles from './SnapForm.module.scss'

type SnapFormProps = {
  titleSuffix: '남기기' | '수정하기'
  initialImgUrl?: string | null
  initialText?: string | null
  handleCancel: () => void
  handleSubmit: (formData: FormData) => void
  style?: React.CSSProperties
}

export default function SnapForm({
  titleSuffix,
  initialImgUrl = null,
  initialText = null,
  handleCancel,
  handleSubmit,
  style,
}: SnapFormProps) {
  const [preview, setPreview] = useState<string | null>(initialImgUrl)

  const snapSubmitHandler = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    const formData = new FormData(event.currentTarget)
    const text = formData.get('text')
    const image = formData.get('image') as File

    const data = new FormData()
    const blob = new Blob([JSON.stringify({ text })], {
      type: 'application/json',
    })

    if ((!image.name && !preview) || (image.name && preview)) {
      data.append('contentImage', image)
    }
    data.append('content', blob)

    handleSubmit(data)
  }

  useEffect(() => {
    document.body.style.overflow = 'hidden'
    return () => {
      document.body.style.overflow = 'unset'
    }
  })

  return (
    <div className={styles.snapFormContainer} style={style}>
      <form onSubmit={snapSubmitHandler}>
        <div className={styles.header}>
          <h1>순간기록 {titleSuffix}</h1>
          <div className={styles.btnBox}>
            <button type='button' onClick={handleCancel} className={styles.cancelBtn}>
              취소
            </button>
            <button type='submit' className={styles.completeBtn}>
              완료
            </button>
          </div>
        </div>
        <ImgInput initialImgUrl={initialImgUrl} preview={preview} setPreview={setPreview} />
        <textarea
          className={styles.textInput}
          id='text'
          name='text'
          defaultValue={initialText === null ? '' : initialText}
        />
      </form>
    </div>
  )
}
