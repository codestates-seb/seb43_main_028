import { axiosInstance, fileAxios } from './instance'

type CreateSnapProps = {
  walkLogId: string
  data: FormData
}
export const createSnap = async ({ walkLogId, data }: CreateSnapProps) => {
  try {
    const response = await fileAxios.post(`/walk-logs/${walkLogId}/contents`, data)
    return response.status === 201 ? 'success' : 'fail'
  } catch (error: unknown) {
    return 'fail'
  }
}

export const patchHistoryItem = async (walkLogId: string, contentId: string, data: FormData) => {
  try {
    const url = `/walk-logs/${walkLogId}/contents/${contentId}`
    const response = await fileAxios.patch(url, data)
    return response.data
  } catch (error: unknown) {
    console.log(error)
    return 'fail'
  }
}

export const deleteHistoryItem = async (walkLogId: string, contentId: string) => {
  try {
    const response = await axiosInstance.delete(`/walk-logs/${walkLogId}/contents/${contentId}`)
    return response.status === 204 ? 'success' : 'fail'
  } catch (error: unknown) {
    console.log(error)
    return 'fail'
  }
}
