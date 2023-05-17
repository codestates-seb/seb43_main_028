import { useState, useRef, useEffect } from 'react'
import styles from './ProfileImgInput.module.scss'
import Icon from '../common/Icon'

type ProfileImgInputProps = {
  imgFile: File | undefined
  setImgFile: React.Dispatch<React.SetStateAction<File | undefined>>
}

export default function ProfileImgInput({ imgFile, setImgFile }: ProfileImgInputProps) {
  const [preview, setPreview] = useState<string>('')
  const inputRef = useRef<HTMLInputElement | null>(null)

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setImgFile(event.target.files?.[0])
  }

  const handleClear = () => {
    if (!inputRef.current) return
    inputRef.current.value = ''
    setImgFile(undefined)
    setPreview('')
  }

  const handleCameraClick = () => {
    inputRef.current?.click()
  }

  useEffect(() => {
    if (!imgFile) return setPreview('')

    const nextPreview = URL.createObjectURL(imgFile)
    setPreview(nextPreview)

    return () => {
      URL.revokeObjectURL(nextPreview)
      setPreview('')
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
