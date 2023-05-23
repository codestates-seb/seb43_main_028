import { useState, useRef, useEffect } from 'react'
import styles from './ImgInput.module.scss'
import Icon from './Icon'

type ImgInputProps = {
  initialImgUrl: string | null
  preview: string | null
  setPreview: React.Dispatch<React.SetStateAction<string | null>>
}

export default function ImgInput({ initialImgUrl, preview, setPreview }: ImgInputProps) {
  const [imgFile, setImgFile] = useState<File | null>(null)
  const inputRef = useRef<HTMLInputElement | null>(null)

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setImgFile(event.target.files?.[0] || null)
  }

  const handleClear = () => {
    if (!inputRef.current) return
    inputRef.current.value = ''
    setImgFile(null)
    setPreview('')
  }

  const handleCameraClick = () => {
    inputRef.current?.click()
  }

  useEffect(() => {
    if (!imgFile && !preview) return setPreview('')
    if (!imgFile) return setPreview(initialImgUrl)

    const nextPreview = URL.createObjectURL(imgFile)
    setPreview(nextPreview)

    return () => {
      URL.revokeObjectURL(nextPreview)
      setPreview(initialImgUrl)
    }
  }, [imgFile])

  return (
    <div className={styles.container}>
      <div className={styles.previewBox}>
        {preview ? (
          <>
            <img src={preview} alt='이미지 미리보기' />
            <button type='button' onClick={handleClear} className={styles.previewClearBtn}>
              <Icon name='close' size={16} />
            </button>
          </>
        ) : (
          <p>
            No
            <br />
            Image
          </p>
        )}
      </div>

      <button type='button' className={styles.cameraBtn} onClick={handleCameraClick}>
        <Icon name='camera-oval' size={48} />
      </button>
      <input
        id='image'
        name='image'
        type='file'
        ref={inputRef}
        onChange={handleChange}
        accept='image/png, image/jpeg'
        className={styles.album}
      />
    </div>
  )
}