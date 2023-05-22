import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { useAtomValue } from 'jotai'
import useRouter from '../hooks/useRouter'
import { getWalkLog, WalkLogType, WalkLogContentType } from '../apis/walkLog'
import WalkHeader from '../components/header/WalkHeader'
import styles from './AfterWalk.module.scss'
import SnapItem from '../components/common/Item/SnapItem'
import { differenceInSeconds } from '../utils/date-fns'
import { deleteSnap, editSnap } from '../apis/snap'
import DropDown from '../components/common/DropDown'
import { userAtom } from '../store/authAtom'

export default function AfterWalk() {
  const { routeTo } = useRouter()
  const { id: walkLogId } = useParams()
  const userInfo = useAtomValue(userAtom)

  const [pubilcOption, setPublicOption] = useState<'PUBLIC' | 'PRIVATE'>(
    userInfo.defaultWalkLogPublicSetting
  )
  const [walkLog, setWalkLog] = useState<WalkLogType | null>(null)
  const [snaps, setSnaps] = useState<WalkLogContentType[]>(walkLog?.walkLogContents || [])

  const createdDate = walkLog && new Date(walkLog.createdAt)

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
    const formData = new FormData(event.currentTarget)
    const message = formData.get('message') as string

    console.log(message)
    console.log(pubilcOption)
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

  if (walkLog === null) return <div>산책 했당~</div>

  return (
    <div>
      <WalkHeader type='AFTER' startedAt={walkLog.createdAt} />
      <form onSubmit={handleStopClick}>
        {/* 맵 이미지 넣기 */}
        <DropDown currentSetting={pubilcOption} onSubmit={updatePublicOption} />
        <input type='text' name='message' id='message' placeholder='한줄메세지 작성' required />
        <button type='submit'>완료</button>
      </form>
      {/* 지도 */}
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
