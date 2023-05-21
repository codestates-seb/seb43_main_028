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

