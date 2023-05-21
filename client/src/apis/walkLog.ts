import { axiosInstance } from './instance'

type StartWalkLogResType = number | null

type StartWalkLogProps = {
  memberId: number
}

export const startWalkLog = async ({
  memberId,
}: StartWalkLogProps): Promise<StartWalkLogResType> => {
  try {
    const startWalkLogRes = await axiosInstance.post('/api/walk-logs', { memberId })
    return startWalkLogRes.data.walkLogId
  } catch (error: unknown) {
    return null
  }
}

export const getAllPublicWalkLogs = async () => {
  try {
    const allPublicWalkLogs = await axiosInstance.get('/walk-logs')
    console.log(allPublicWalkLogs)
  } catch (error) {
    console.error(error)
  }
}

export const aaa = true
