import { axiosInstance } from './instance'

type StartWalkLogResType = {
  walkLogId: number
}

export const startWalkLog = async (memberId: number): Promise<StartWalkLogResType> => {
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

type WalkLogContentType = {
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

export const getWalkLog = async (walkLogId: number): Promise<WalkLogType | null> => {
  try {
    const { data } = await axiosInstance.get(`/walk-logs/${walkLogId}`)
    return data as WalkLogType
  } catch (error: unknown) {
    return null
  }
}
