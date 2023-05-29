import { useRef, useEffect } from 'react'
import styles from './ProfileImgInput.module.scss'
import Icon from '../common/Icon'

type ProfileImgInputProps = {
  imgFile: File | undefined
  setImgFile: React.Dispatch<React.SetStateAction<File | undefined>>
  profileImage: string
  preview: string | null
  setPreview: React.Dispatch<React.SetStateAction<string>>
}

export default function ProfileImgInput({
  imgFile,
  setImgFile,
  profileImage,
  preview,
  setPreview,
}: ProfileImgInputProps) {
  const inputRef = useRef<HTMLInputElement | null>(null)

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const currentImgFile = event.target.files?.[0]
    if (currentImgFile) {
      const fileSize = currentImgFile.size / (1024 * 1024)
      if (fileSize > 5) {
        alert('5mb 이상의 파일은 업로드할 수 없습니다.')
        return
      }
      setImgFile(currentImgFile)
    } else {
      alert('파일을 찾을 수 없습니다.')
    }
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
    if (!imgFile && !preview) return setPreview('')
    if (!imgFile) return setPreview(profileImage)

    const nextPreview = URL.createObjectURL(imgFile)
    setPreview(nextPreview)

    return () => {
      URL.revokeObjectURL(nextPreview)
      setPreview(profileImage)
    }
  }, [imgFile])

  return (
    <div className={styles.container}>
      <div className={styles.previewBox}>
        {preview ? (
          <>
            <div className={styles.imageWrapper}>
              <img src={preview} alt='이미지 미리보기' />
            </div>
            <button type='button' onClick={handleClear} className={styles.previewClearBtn}>
              <Icon name='close' size={16} />
            </button>
          </>
        ) : (
          <div className={styles.imageWrapper}>
            <p>
              No
              <br />
              Image
            </p>
          </div>
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
