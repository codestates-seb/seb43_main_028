import { useEffect, useRef } from 'react'
import styles from './Camera.module.scss'

function Camera() {
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

  const takePhoto = () => {
    console.log('촬영')
  }

  const handleCancel = () => {
    console.log('카메라 취소 입력폼 돌아가기')
  }

  useEffect(() => {
    getVideo()

    return () => {
      if (videoRef.current?.srcObject instanceof MediaStream) {
        console.log('이전 미디어 스트림 삭제') // 이전 미디어 스트림 삭제
        const videoTrack = videoRef.current.srcObject?.getTracks()
        videoTrack?.forEach((track: MediaStreamTrack) => {
          track.stop()
        })
      }
    }
  }, [photoRef])

  return (
    <div>
      <video ref={videoRef} autoPlay playsInline className={styles.camera}>
        <track kind='captions' />
      </video>
      <div>
        <button type='button' onClick={takePhoto} className={styles.shotBtn} aria-label='shotBtn' />
        <button type='button' onClick={handleCancel} className={styles.cancelBtn}>
          X
        </button>
      </div>
    </div>
  )
}

export default Camera
