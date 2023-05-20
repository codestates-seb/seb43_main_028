import { axiosInstance, fileAxios } from './instance'
import { getMonth, getYear, startOfToday } from '../utils/date-fns'

export const getHistory = async (walkLogId: string) => {
  try {
    const response = await axiosInstance.get(`/walk-logs/${walkLogId}`)
    return response.data
  } catch (error: unknown) {
    console.log(error)
    return 'fail'
  }
}

export const patchHistory = async (walkLogId: string, data: string) => {
  try {
    const response = await axiosInstance.patch(`/walk-logs/${walkLogId}`, data)
    return response.data
  } catch (error: unknown) {
    console.log(error)
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

export const deleteHistory = async (walkLogId: string) => {
  try {
    const response = await axiosInstance.delete(`/walk-logs/${walkLogId}`)
    return response.status === 204 ? 'success' : 'fail'
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

export const getHistoryList = async (
  memberId: number,
  page = 1,
  year: null | number = null,
  month: null | number = null,
  day: null | number = null
) => {
  try {
    const url = `/members/${memberId}/walk-logs`
    const response = await axiosInstance.get(url, {
      params: { page, year, month, day },
    })
    return response.data
  } catch (error: unknown) {
    console.log(error)
    return 'fail'
  }
}

export const getHistoryCalendarList = async (
  memberId: number,
  year = getYear(startOfToday()),
  month = getMonth(startOfToday()) + 1
) => {
  try {
    const response = await axiosInstance.get(`/members/${memberId}/walk-logs/calendar`, {
      params: { year, month },
    })
    return response.data
  } catch (error: unknown) {
    console.log(error)
    return `fail`
  }
}
