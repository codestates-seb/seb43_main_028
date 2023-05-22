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

type DeleteSnapProps = {
  walkLogId: string
  snapId: number
}
export const deleteSnap = async ({ walkLogId, snapId }: DeleteSnapProps) => {
  try {
    const response = await axiosInstance.delete(`/walk-logs/${walkLogId}/contents/${snapId}`)
    return response.status === 204 ? 'success' : 'fail'
  } catch (error: unknown) {
    return 'fail'
  }
}

type EditSnapProps = {
  walkLogId: string
  snapId: number
  data: FormData
}
export const editSnap = async ({ walkLogId, snapId, data }: EditSnapProps) => {
  try {
    const url = `/walk-logs/${walkLogId}/contents/${snapId}`
    const response = await fileAxios.patch(url, data)
    return response.data
  } catch (error: unknown) {
    return null
  }
}
