import { axiosInstance, fileAxios } from './instance'

export type WalkLogContentType = {
  walkLogContentId: number
  text: string
  createdAt: string
  imageUrl: string | null
}

type CoordinateType = {
  coordinateId: number
  lat: number
  lng: number
  createdAt: string
}

export type WalkLogType = {
  walkLogId: number
  createdAt: string
  endAt: string
  memberId: number
  nickname: string
  profileImage: string | null
  message: string
  mapImage: string | null
  walkLogStatus: 'RECORDING' | 'STOP'
  walkLogPublicSetting: 'PRIVATE' | 'PUBLIC'
  coordinates: CoordinateType[]
  walkLogContents: WalkLogContentType[]
}

type StartWalkLogResType = {
  walkLogId: number
}

export const startWalkLog = async ({
  memberId,
}: StartWalkLogProps): Promise<StartWalkLogResType> => {

  try {
    const {
      data: { walkLogId },
    } = await axiosInstance.post('/walk-logs', { memberId })
    return {
      walkLogId,
    }
  } catch (error: unknown) {
    return {
      walkLogId: -1,
    }
  }
}

export const getWalkLog = async (walkLogId: number): Promise<WalkLogType | null> => {
  try {
    const { data } = await axiosInstance.get(`/walk-logs/${walkLogId}`)
    return data as WalkLogType
  } catch (error: unknown) {
    return null
  }
}


type StopWalkLogProps = {
  walkLogId: string
  data: {
    endPost: {
      message: string
      walkLogPublicSetting: 'PRIVATE' | 'PUBLIC'
    }
    mapImage: File | null
  }
}

export const stopWalkLog = async ({ walkLogId, data }: StopWalkLogProps): Promise<boolean> => {
  try {
    const response = await fileAxios.post(`/walk-logs/${walkLogId}`, { ...data })
    console.log(response)
    return true
  } catch (error: unknown) {
    return false
  }
}

type GetAllPublicWalkLogsResType = {
  data: []
  pageInfo: {
    page: number
    size: number
    totalElements: number
    totalPages: number
  }
} | null

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

