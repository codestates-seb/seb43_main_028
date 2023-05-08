import { useState, useRef, useEffect } from 'react'
import Camera from './Camera'
import styles from './ImgInput.module.scss'

export default function ImgInput() {
  const [preview, setPreview] = useState<string | undefined>('')
  const [imgFile, setImgFile] = useState<File | undefined>()
  const inputRef = useRef<HTMLInputElement | null>(null)

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setImgFile(event.target.files?.[0])
  }

  const handleClear = () => {
    if (!inputRef.current) return
    inputRef.current.value = ''
    setImgFile(undefined)
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
        {imgFile ? <img src={preview} alt='이미지 미리보기' /> : 'No Image'}
      </div>
      {imgFile && (
        <button type='button' onClick={handleClear}>
          X
        </button>
      )}
      <button type='button' className={styles.cameraBtn}>
        <Camera />
        <input
          id='photo'
          type='file'
          ref={inputRef}
          onChange={handleChange}
          accept='image/png, image/jpeg'
        />
      </button>
    </div>
  )
}
