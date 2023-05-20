import axios from 'axios'
import { getMonth, getYear, startOfToday } from '../utils/date-fns'

export const getHistory = async (walkLogId: string) => {
  try {
    const response = await axios.get(`/api/walk-logs/${walkLogId}`, {
      headers: {
        'Content-Type': 'application/json',
        'ngrok-skip-browser-warning': '69420',
      },
    })
    return response.data
  } catch (error: unknown) {
    console.log(error)
    return 'fail'
  }
}

export const patchHistory = async (walkLogId: string, data: string) => {
  try {
    const response = await axios.patch(`/api/walk-logs/${walkLogId}`, data, {
      headers: {
        'Content-Type': 'application/json',
      },
    })
    return response.data
  } catch (error: unknown) {
    console.log(error)
    return 'fail'
  }
}

export const patchHistoryItem = async (walkLogId: string, contentId: string, data: FormData) => {
  try {
    const url = `/api/walk-logs/${walkLogId}/contents/${contentId}`
    const response = await axios.patch(url, data, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
    return response.data
  } catch (error: unknown) {
    console.log(error)
    return 'fail'
  }
}

export const deleteHistory = async (walkLogId: string) => {
  try {
    const response = await axios.delete(`/api/walk-logs/${walkLogId}`)
    return response.status === 204 ? 'success' : 'fail'
  } catch (error: unknown) {
    console.log(error)
    return 'fail'
  }
}

export const deleteHistoryItem = async (walkLogId: string, contentId: string) => {
  try {
    const response = await axios.delete(`/api/walk-logs/${walkLogId}/contents/${contentId}`)
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
    const url = `/api/members/${memberId}/walk-logs`
    const response = await axios.get(url, {
      headers: {
        'Content-Type': 'application/json',
        'ngrok-skip-browser-warning': '69420',
      },
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
    const response = await axios.get(`/api/members/${memberId}/walk-logs/calendar`, {
      headers: {
        'Content-Type': 'application/json',
        'ngrok-skip-browser-warning': '69420',
      },
      params: { year, month },
    })
    return response.data
  } catch (error: unknown) {
    console.log(error)
    return `fail`
  }
}
