import { useState, useRef, useEffect } from 'react'
import styles from './ImgInput.module.scss'
import Modal from '../common/Modal'
import Camera from './Camera'
import Icon from '../common/Icon'

export default function ImgInput() {
  const [preview, setPreview] = useState<string>('')
  const [imgFile, setImgFile] = useState<File | undefined>()
  const [modal, setModal] = useState<boolean>(false)
  const [camera, setCamera] = useState<boolean>(false)
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

  const handleModal = () => {
    setModal(!modal)
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

  const modalData = {
    title: '옵션 선택',
    options: [
      {
        id: 0,
        label: '사진 촬영',
        handleClick: () => {
          setCamera(true)
          setModal(false)
        },
      },
      {
        id: 1,
        label: '앨범에서 가져오기',
        handleClick: () => {
          setModal(false)
          inputRef.current?.click()
        },
      },
    ],
  }

  return (
    <div className={styles.container}>
      <div className={styles.previewBox}>
        {preview ? (
          <>
            <img src={preview} alt='이미지 미리보기' />
            <button type='button' onClick={handleClear} className={styles.previewClearBtn}>
              X
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

      <button type='button' className={styles.cameraBtn} onClick={handleModal}>
        <Icon name='camera-oval' size={48} />
      </button>
      {modal && <Modal modalData={modalData} onClose={handleModal} />}
      {!modal && camera && <Camera setCamera={setCamera} setPreview={setPreview} />}

      <input
        id='photo'
        type='file'
        ref={inputRef}
        onChange={handleChange}
        accept='image/png, image/jpeg'
        className={styles.album}
      />
    </div>
  )
}
