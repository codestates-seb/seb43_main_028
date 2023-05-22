import { axiosInstance } from './instance'

type StartWalkLogResType = number | null

type StartWalkLogProps = {
  memberId: number
}

type GetAllPublicWalkLogsParamType = number
type GetAllPublicWalkLogsResType = {
  data: []
  pageInfo: {
    page: number
    size: number
    totalElements: number
    totalPages: number
  }
} | null

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

export const getAllPublicWalkLogs = async (page = 1): Promise<GetAllPublicWalkLogsResType> => {
  try {
    const response = await axiosInstance.get('/walk-logs', { params: { page } })
    const allPublicWalkLogs = response.data
    return allPublicWalkLogs
  } catch (error) {
    console.error(error)
    return null
  }
}

export const aaa = true
