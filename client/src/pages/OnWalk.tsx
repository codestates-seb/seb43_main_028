import { useEffect, useRef, useState } from 'react'
import { useParams } from 'react-router-dom'
import { Stomp, CompatClient } from '@stomp/stompjs'
import SockJS from 'sockjs-client'
import { getWalkLog, WalkLogType, WalkLogContentType } from '../apis/walkLog'
import WalkHeader from '../components/header/WalkHeader'
import useMapRef from '../hooks/useMapRef'
import useRouter from '../hooks/useRouter'
import Icon from '../components/common/Icon'
import SnapItem from '../components/common/Item/SnapItem'
import SnapForm from '../components/common/SnapForm'
import Modal from '../components/common/Modal'
import { createSnap, deleteSnap, editSnap } from '../apis/snap'
import { differenceInSeconds } from '../utils/date-fns'
import LiveMap from '../components/common/Map/LiveMap'
import styles from './OnWalk.module.scss'
import { getDistanceBetweenPosition } from '../utils/position'
import { getAccessTokenFromLocalStorage } from '../utils/accessTokenHandler'
import OnWalkLoading from './loadingPage/OnWalkLoading'
// import Spinner from './loadingPage/Spinner'

export default function OnWalk() {
  const { routeTo } = useRouter()
  const { id: walkLogId } = useParams()

  const [walkLog, setWalkLog] = useState<WalkLogType | null>(null)
  const [snaps, setSnaps] = useState<WalkLogContentType[]>(walkLog?.walkLogContents || [])

  const [isSnapFormOpen, setIsSnapFormOpen] = useState(false)
  const [isStopModalOpen, setIsStopModalOpen] = useState(false)

  // const [isLoading, setIsLoading] = useState<boolean>(true)
  // const [textIndex, setTextIndex] = useState<number>(-1)

  // const startTextArr = [
  //   // { id: -1, text: '잠시 후 산책 기록을 시작합니다!' },
  //   { id: 0, text: '오늘 하루를 정리하며 걸어보는 건 어떨까요?' },
  //   { id: 1, text: '요즘 연락이 뜸했던 친구에게 전화를 걸어보는 건 어떨까요?' },
  //   { id: 2, text: '내일 하루를 미리 그려보며 걷는 건 어떨까요?' },
  //   { id: 3, text: '동네 친구를 불러내 함께 걸어보는 건 어떠세요?' },
  //   { id: 4, text: '시간을 버리듯이 걷다 보면 오히려 많은 걸 얻게 될지도 몰라요.' },
  // ]

  const mapRef = useMapRef()

  const clientWS = useRef<CompatClient | null>(null)

  const [path, setPath] = useState<google.maps.LatLngLiteral[]>(walkLog?.coordinates || [])

  const createdDate = walkLog && new Date(walkLog.createdAt)

  const stopModalData = {
    title: '걷기를 종료하시겠어요?',
    options: [
      { id: 1, label: '걷기 종료', handleClick: () => stopWalk() },
      { id: 2, label: '계속 걷기', handleClick: () => setIsStopModalOpen(false) },
    ],
  }

  const takeSnapClick = () => setIsSnapFormOpen(true)

  const stopWalk = async () => {
    if (walkLogId === undefined) return
    routeTo(`/afterwalk/${walkLogId}`)
  }

  const submitSnap = async (formData: FormData) => {
    if (walkLogId === undefined) return
    const response = await createSnap({ walkLogId, data: formData })
    if (response === 'success') {
      getWalkLogData()
      setIsSnapFormOpen(false)
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

  const watchCurrentPosition = () => {
    const watchId = navigator.geolocation.watchPosition(
      newPosition => {
        const { latitude, longitude } = newPosition.coords
        const watchedPosition = { lat: latitude, lng: longitude }
        const lastPosition = path[path.length - 1]
        if (!lastPosition || getDistanceBetweenPosition(lastPosition, watchedPosition) > 2) {
          setPath(prev => [...prev, watchedPosition])
          clientWS.current?.send('/pub/walk-logs', {}, JSON.stringify(watchedPosition))
        }
      },
      error => console.log(error),
      { enableHighAccuracy: true, maximumAge: 0, timeout: 5000 }
    )

    return {
      watchId,
      clearWatchPosition: () => {
        navigator.geolocation.clearWatch(watchId)
      },
    }
  }

  useEffect(() => {
    // setTimeout(() => {
    //   setTextIndex(-1)
    //   setIsLoading(false)
    // }, 5000)
    getWalkLogData()
    const { clearWatchPosition } = watchCurrentPosition()

    clientWS.current = Stomp.over(() => {
      const sock = new SockJS('https://was.would-you-walk.com/ws/walk-logs')
      return sock
    })

    clientWS.current.connect(
      { Authorization: getAccessTokenFromLocalStorage() },
      () => {
        clientWS.current?.subscribe(`/sub/${walkLogId}`, message => {
          console.log(message.body)
        })
      },
      (error: any) => {
        console.log(error)
      }
    )

    return () => {
      clearWatchPosition()
      clientWS.current?.disconnect()
    }
  }, [])

  // if (isLoading)
  //   return <Spinner startTextArr={startTextArr} textIndex={textIndex} setTextIndex={setTextIndex} />
  if (!walkLog) return <OnWalkLoading />

  return (
    <>
      {isSnapFormOpen && (
        <SnapForm
          titleSuffix='남기기'
          handleCancel={() => setIsSnapFormOpen(false)}
          handleSubmit={submitSnap}
        />
      )}

      {isStopModalOpen && (
        <Modal modalData={stopModalData} onClose={() => setIsStopModalOpen(false)} />
      )}

      <WalkHeader
        type='ON'
        startedAt={walkLog?.createdAt}
        handleFinishClick={() => setIsStopModalOpen(true)}
      />

      <LiveMap ref={mapRef} path={path.length > 0 ? path : null} />

      <div className={styles.snapBox}>
        <button className={styles.snapbutton} type='button' onClick={takeSnapClick}>
          순간기록 남기기
          <Icon name='camera-color' />
        </button>
        <ul className={styles.snaplist}>
          {(snaps &&
            (snaps.length === 0 ? (
              <div>순간기록은 최대 5개까지 작성하실 수 있습니다.</div>
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
    </>
  )
}
