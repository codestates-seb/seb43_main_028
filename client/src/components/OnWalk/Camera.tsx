import { useEffect, useRef, useState } from 'react'
import styles from './Camera.module.scss'

function Camera() {
  const [hasPhoto, setHasPhoto] = useState(false)
  const [cameraOn, setCameraOn] = useState(true)

  const photoRef = useRef<HTMLCanvasElement>(null)
  const videoRef = useRef<HTMLVideoElement>(null)

  const getVideo = () => {
    navigator.mediaDevices
      .getUserMedia({
        video: { facingMode: 'user' },
        // 후면 카메라 video: { facingMode: { exact: "environment" } }
      })
      .then(stream => {
        if (videoRef.current) {
          videoRef.current.srcObject = stream
        }
      })
      .catch(err => console.error(err))
  }

  const handleCancel = () => {
    setCameraOn(false)
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
    <div className={cameraOn ? styles.container : styles.off}>
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
      </div>
    </div>
  )
}

export default Camera
