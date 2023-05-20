import axios from 'axios'

type StartWalkLogResType = number | null

type StartWalkLogProps = {
  memberId: number
}

export const startWalkLog = async ({
  memberId,
}: StartWalkLogProps): Promise<StartWalkLogResType> => {
  try {
    const startWalkLogRes = await axios.post('/api/walk-logs', { memberId })
    return startWalkLogRes.data.walkLogId
  } catch (error: unknown) {
    return null
  }
}

export const aaa = true
