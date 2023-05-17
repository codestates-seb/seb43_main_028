import { useEffect, useRef, useState } from 'react'
import styles from './Camera.module.scss'

type CameraProps = {
  setCamera: React.Dispatch<React.SetStateAction<boolean>>
  setPreview: React.Dispatch<React.SetStateAction<string>>
}

function Camera({ setCamera, setPreview }: CameraProps) {
  const [hasPhoto, setHasPhoto] = useState(false)

  const photoRef = useRef<HTMLCanvasElement>(null)
  const videoRef = useRef<HTMLVideoElement>(null)

  const getVideo = () => {
    navigator.mediaDevices
      .getUserMedia({
        video: { facingMode: { exact: 'environment' } },
        // 전면 카메라 video: { facingMode: 'user' },
      })
      .then(stream => {
        if (videoRef.current) {
          videoRef.current.srcObject = stream
        }
      })
      .catch(err => console.error(err))
  }

  const handleCancel = () => {
    setCamera(false)
  }

  const takePhoto = () => {
    if (photoRef.current && videoRef.current) {
      const photo = photoRef.current
      photo.width = videoRef.current.offsetWidth
      photo.height = videoRef.current.offsetHeight

      const context = photo.getContext('2d')
      context?.drawImage(videoRef.current, 0, 0, photo.width, photo.height)
      setHasPhoto(true)
    }
  }

  const cancelPhoto = () => {
    if (photoRef.current) {
      const photo = photoRef.current
      const context = photo.getContext('2d')

      context?.clearRect(0, 0, photo.width, photo.height)

      setHasPhoto(false)
    }
  }

  const savePhoto = () => {
    if (photoRef.current) {
      const url = photoRef.current.toDataURL()
      setPreview(url)
      setCamera(false)
    }
  }

  useEffect(() => {
    getVideo()

    return () => {
      if (videoRef.current?.srcObject instanceof MediaStream) {
        const videoTrack = videoRef.current.srcObject?.getTracks()
        videoTrack?.forEach((track: MediaStreamTrack) => {
          track.stop()
        })
      }
    }
  }, [photoRef])

  return (
    <div className={styles.container}>
      <div className={hasPhoto ? styles.hidden : ''}>
        <video ref={videoRef} autoPlay playsInline className={styles.camera}>
          <track kind='captions' />
        </video>
        <div>
          <button
            type='button'
            onClick={takePhoto}
            className={styles.shotBtn}
            aria-label='shot button'
          />
          <button type='button' onClick={handleCancel} className={styles.cancelBtn}>
            X
          </button>
        </div>
      </div>
      <div className={hasPhoto ? '' : styles.hidden}>
        <canvas ref={photoRef} className={styles.photo} />
        <button type='button' onClick={cancelPhoto} className={styles.cancelBtn}>
          X
        </button>
        <button type='button' onClick={savePhoto} className={styles.saveBtn}>
          SAVE
        </button>
      </div>
    </div>
  )
}

export default Camera
