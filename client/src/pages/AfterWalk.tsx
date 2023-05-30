import { useEffect, useRef, useState } from 'react'
import { useParams } from 'react-router-dom'
import { useAtomValue } from 'jotai'
import useRouter from '../hooks/useRouter'
// import useDrawPolyline from '../hooks/useDrawPolyline'
import { getWalkLog, WalkLogType, WalkLogContentType, stopWalkLog } from '../apis/walkLog'
import WalkHeader from '../components/header/WalkHeader'
import styles from './AfterWalk.module.scss'
import SnapItem from '../components/common/Item/SnapItem'
import { differenceInSeconds } from '../utils/date-fns'
import { deleteSnap, editSnap } from '../apis/snap'
import DropDown from '../components/common/DropDown'
import { UserInfoAtomType, userInfoAtom } from '../store/authAtom'
import { convertImageFromDataURL } from '../utils/imageConvertor'
import StaticPathMap from '../components/common/Map/StaticPathMap'
import useMapRef from '../hooks/useMapRef'
import AfterWalkLoading from './loadingPage/AfterWalkLoading'

export default function AfterWalk() {
  const { routeTo } = useRouter()
  const { id: walkLogId } = useParams()
  const userInfo = useAtomValue(userInfoAtom) as UserInfoAtomType

  const [pubilcOption, setPublicOption] = useState<'PUBLIC' | 'PRIVATE'>(
    userInfo.defaultWalkLogPublicSetting
  )
  const [walkLog, setWalkLog] = useState<WalkLogType | null>(null)
  const [snaps, setSnaps] = useState<WalkLogContentType[]>(walkLog?.walkLogContents || [])

  const createdDate = walkLog && new Date(walkLog.createdAt)

  // const { img, canvasRef } = useDrawPolyline(walkLog?.coordinates || [])

  const mapRef = useMapRef()

  const [mapImg, setMapImg] = useState<string | null>(null)
  const canvasRef = useRef<HTMLCanvasElement | null>(null)

  const getWalkLogData = async () => {
    const data = await getWalkLog(Number(walkLogId))
    if (data?.walkLogStatus === 'STOP') {
      routeTo('/')
      return
    }
    if (data) {
      setWalkLog(data)
      setSnaps(data.walkLogContents)
    }
  }

  const updatePublicOption = () => {
    const next = pubilcOption === 'PUBLIC' ? 'PRIVATE' : 'PUBLIC'
    setPublicOption(next)
  }

  const handleStopClick = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    if (walkLogId === undefined) return
    if (!mapImg) return

    const formData = new FormData(event.currentTarget)
    const message = formData.get('message') as string

    const data = new FormData()
    const blob = new Blob(
      [JSON.stringify({ message, walkLogPublicSetting: pubilcOption, region: '29304' })],
      { type: 'application/json' }
    )

    data.append('endPost', blob)
    data.append('mapImage', convertImageFromDataURL(mapImg))

    const response = await stopWalkLog({ walkLogId, data })
    if (response) {
      routeTo('/')
    }
  }

  const handleEditSnap = async (snapId: number, data: FormData) => {
    if (walkLogId === undefined) return
    const updated = await editSnap({ walkLogId, snapId, data })
    if (updated) {
      setSnaps(
        snaps.map(snap =>
          snap.walkLogContentId === snapId
            ? {
                walkLogContentId: updated.walkLogContentId,
                text: updated.text,
                createdAt: updated.createdAt,
                imageUrl: updated.imageUrl,
              }
            : snap
        )
      )
    }
  }

  const handleDeleteSnap = async (snapId: number) => {
    if (walkLogId === undefined) return
    const response = await deleteSnap({ walkLogId, snapId })
    if (response === 'success') {
      setSnaps(snaps.filter(snap => snap.walkLogContentId !== Number(snapId)))
    }
  }

  useEffect(() => {
    getWalkLogData()
  }, [])

  function drawLines(
    canvas: HTMLCanvasElement,
    context: CanvasRenderingContext2D,
    coordinates: { lat: number; lng: number }[]
  ) {
    context.clearRect(0, 0, canvas.width, canvas.height) // 캔버스 초기화
    context.fillStyle = '#f3f4f6' // 캔버스 배경색 설정
    context.fillRect(0, 0, canvas.width, canvas.height) // 배경색으로 사각형 그리기

    if (coordinates.length > 0) {
      const minLng = Math.min(...coordinates.map(coord => coord.lng))
      const maxLng = Math.max(...coordinates.map(coord => coord.lng))
      const minLat = Math.min(...coordinates.map(coord => coord.lat))
      const maxLat = Math.max(...coordinates.map(coord => coord.lat))

      const pathWidth = canvas.width * 0.6 // 경로의 가로 길이 (캔버스 너비의 60%)
      const pathHeight = canvas.height * 0.6 // 경로의 세로 길이 (캔버스 높이의 60%)
      const offsetX = (canvas.width - pathWidth) / 2 // X 좌표 오프셋
      const offsetY = (canvas.height - pathHeight) / 2 // Y 좌표 오프셋

      context.beginPath()
      for (let i = 0; i < coordinates.length; i += 1) {
        const { lat, lng } = coordinates[i]

        // 좌표를 Canvas 좌표로 변환
        const x = offsetX + ((lng - minLng) / (maxLng - minLng)) * pathWidth
        const y = offsetY + ((lat - minLat) / (maxLat - minLat)) * pathHeight

        if (i === 0) {
          context.moveTo(x, y)
        } else {
          context.lineTo(x, y)
        }
      }

      context.strokeStyle = '#8cff9e'
      context.lineWidth = 3
      context.stroke()
    }
  }

  useEffect(() => {
    if (canvasRef.current && walkLog) {
      const canvas = canvasRef.current
      const context = canvas.getContext('2d')
      drawLines(canvas, context as CanvasRenderingContext2D, walkLog.coordinates)
      canvas.width = 64
      canvas.height = 64
      const polylineImg = canvas.toDataURL('image/png')
      setMapImg(polylineImg)
    }
  }, [canvasRef, walkLog])

  if (walkLog === null) return <AfterWalkLoading />

  return (
    <div>
      <WalkHeader type='AFTER' startedAt={walkLog.createdAt} />
      <form className={styles.stopWalkForm} onSubmit={handleStopClick}>
        <canvas ref={canvasRef} className={styles.canvasElement} />
        <div className={styles.publicOptionBox}>
          <span>기록공개 설정</span>
          <DropDown currentSetting={pubilcOption} onSubmit={updatePublicOption} />
        </div>
        <input
          className={styles.messageInput}
          type='text'
          name='message'
          id='message'
          placeholder='한줄메세지 작성'
          required
        />
        <button className={styles.stopWalkFormButton} type='submit'>
          완료
        </button>
      </form>

      <StaticPathMap ref={mapRef} path={walkLog.coordinates} />

      <div className={styles.snapBox}>
        <ul className={styles.snaplist}>
          {(snaps &&
            (snaps.length === 0 ? (
              <div>작성하신 순간기록이 없습니다.</div>
            ) : (
              snaps.map(({ walkLogContentId, text, createdAt, imageUrl }) => {
                const seconds = differenceInSeconds(new Date(createdAt), createdDate as Date)
                return (
                  <SnapItem
                    key={walkLogContentId}
                    snapId={walkLogContentId}
                    content={text}
                    seconds={seconds}
                    imageUrl={imageUrl}
                    handleEdit={handleEditSnap}
                    handleDelete={handleDeleteSnap}
                  />
                )
              })
            ))) || <div>순간기록을 불러오는 중입니다.</div>}
        </ul>
      </div>
    </div>
  )
}
