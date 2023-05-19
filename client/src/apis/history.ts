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

export const patchHistoryMessage = async (walkLogId: string, data: string) => {
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

export const deleteHistory = async (walkLogId: string) => {
  try {
    const response = await axios.delete(`/api/walk-logs/${walkLogId}`)
    console.log('삭제 결과', response)
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

export const getHistoryList = async (memberId: number, page = 1) => {
  try {
    const url = `/api/members/${memberId}/walk-logs`
    const response = await axios.get(url, {
      headers: {
        'Content-Type': 'application/json',
        'ngrok-skip-browser-warning': '69420',
      },
      params: {
        page,
      },
    })
    return response.data
  } catch (error: unknown) {
    console.log(error)
    return 'fail'
  }
}

const thisYear = getYear(startOfToday())
const thisMonth = getMonth(startOfToday()) + 1

export const getHistoryCalendarList = async (
  memberId: number,
  year = thisYear,
  month = thisMonth
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
