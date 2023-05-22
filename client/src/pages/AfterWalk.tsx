import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import useRouter from '../hooks/useRouter'
import { getWalkLog, WalkLogType, WalkLogContentType } from '../apis/walkLog'

export default function AfterWalk() {
  const { routeTo } = useRouter()
  const { id: walkLogId } = useParams()

  const [walkLog, setWalkLog] = useState<WalkLogType | null>(null)
  const [snaps, setSnaps] = useState<WalkLogContentType[]>(walkLog?.walkLogContents || [])

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

  // useEffect(() => {
  //   getWalkLogData()
  // }, [])

  return <div>AfterWalk</div>
}
