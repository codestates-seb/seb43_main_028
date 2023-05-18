import { useState, useRef, useEffect } from 'react'
import imageCompression from 'browser-image-compression'
import styles from './ProfileImgInput.module.scss'
import Icon from '../common/Icon'

type ProfileImgInputProps = {
  imgFile: File | undefined
  setImgFile: React.Dispatch<React.SetStateAction<File | undefined>>
}

export default function ProfileImgInput({ imgFile, setImgFile }: ProfileImgInputProps) {
  const [preview, setPreview] = useState<string>('')
  const inputRef = useRef<HTMLInputElement | null>(null)

  // const imageCompress = async (event: React.ChangeEvent<HTMLInputElement>) => {
  //   const file = event.target.files?.[0]
  //   const options = {
  //     maxSizeMB: 0.2, // 이미지 최대 용량
  //     maxWidthOrHeight: 1920, // 최대 넓이(혹은 높이)
  //     useWebWorker: true,
  //   }
  //   try {
  //     if (file) {
  //       const compressedFile = await imageCompression(file, options)
  //       setImgFile(compressedFile)
  //     } else {
  //       alert('no file')
  //     }
  //   } catch (error) {
  //     console.log(error)
  //   }
  // }

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    // imageCompress(event)
    setImgFile(event.target.files?.[0])
  }

  const handleClear = () => {
    if (!inputRef.current) return
    inputRef.current.value = ''
    setImgFile(undefined)
    setPreview('')
  }

  const handleCameraClick = () => {
    console.log(inputRef.current)
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
            <div className={styles.imageWrapper}>
              <img src={preview} alt='이미지 미리보기' />
            </div>
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
