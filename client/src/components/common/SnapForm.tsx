import { useEffect } from 'react'
import ImgInput from './ImgInput'
import styles from './SnapForm.module.scss'

type SnapFormProps = {
  titleSuffix: '남기기' | '수정하기'
  initialImgUrl?: string | null
  initialText?: string | null
  handleCancel: () => void
  handleSubmit: (formData: FormData) => void
}

export default function SnapForm({
  titleSuffix,
  initialImgUrl = null,
  initialText = null,
  handleCancel,
  handleSubmit,
}: SnapFormProps) {
  const snapSubmitHandler = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    const formData = new FormData(event.currentTarget)
    const text = formData.get('text')
    const image = formData.get('image')

    const data = new FormData()
    const blob = new Blob([JSON.stringify({ text })], {
      type: 'application/json',
    })

    data.append('content', blob)
    if (image) data.append('contentImage', image)
    handleSubmit(data)
  }

  useEffect(() => {
    document.body.style.overflow = 'hidden'
    return () => {
      document.body.style.overflow = 'unset'
    }
  })

  return (
    <div className={styles.container}>
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
        <ImgInput initialValue={initialImgUrl} />
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
