import {
  getDate,
  endOfMonth,
  getDay,
  startOfMonth,
  getWeeksInMonth,
  eachDayOfInterval,
} from './date-fns'

const A_MINUTE = 1000 * 60
const AN_HOUR = 1000 * 60 * 60
const A_SECOND = 1000

export function passedHourMinuteSecondFormat(timeDiff: number) {
  const hour = Math.floor(timeDiff / AN_HOUR)
  const restOfHour = timeDiff % AN_HOUR
  const minute = Math.floor(restOfHour / A_MINUTE)
  const restOfminute = restOfHour % A_MINUTE
  const second = Math.floor(restOfminute / A_SECOND)

  const formattedHour = hour ? `${hour}시간` : ''
  const formattedMinute = minute ? `${minute}분` : ''
  const formattedSecond = second ? `${second}초` : ''

  return (
    !formattedHour && !formattedMinute ? formattedSecond : `${formattedHour} ${formattedMinute}`
  ).trim()
}

export function dateFormat(date: Date) {
  const month: number = date.getMonth() + 1
  const day: number = date.getDate()

  const formattedMonth: string = month >= 10 ? `${month}` : `0${month}`
  const formattedDay: string = day >= 10 ? `${day}` : `0${day}`

  return `${date.getFullYear()}-${formattedMonth}-${formattedDay}`
}

export function timeFormat(date: Date) {
  const hour: number = date.getHours()
  const minute: number = date.getMinutes()
  const second: number = date.getSeconds()

  const formattedHour: string = hour >= 10 ? `${hour}` : `0${hour}`
  const formattedMinute: string = minute >= 10 ? `${minute}` : `0${minute}`
  const formattedSecond: string = second >= 10 ? `${second}` : `0${second}`

  return formattedHour === '00'
    ? `${formattedMinute}:${formattedSecond}`
    : `${formattedHour}:${formattedMinute}:${formattedSecond}`
}

export function timerFormat(seconds: number) {
  const hour: number = Math.floor(seconds / 3600)
  const minute: number = Math.floor((seconds % 3600) / 60)
  const second: number = Math.floor((seconds % 3600) % 60)

  const formattedHour: string = hour >= 10 ? `${hour}` : `0${hour}`
  const formattedMinute: string = minute >= 10 ? `${minute}` : `0${minute}`
  const formattedSecond: string = second >= 10 ? `${second}` : `0${second}`

  return formattedHour === '00'
    ? `${formattedMinute}:${formattedSecond}`
    : `${formattedHour}:${formattedMinute}:${formattedSecond}`
}

export function getWeekRows(date: Date): (0 | Date)[][] {
  const lastDate = getDate(endOfMonth(date))
  const startBlankCount = getDay(startOfMonth(date))
  const endBlankCount = 7 - ((startBlankCount + lastDate) % 7)
  const weekCount = getWeeksInMonth(date)

  const allDates = [
    ...Array(startBlankCount).fill(0),
    ...eachDayOfInterval({
      start: startOfMonth(date),
      end: endOfMonth(date),
    }),
    ...Array(endBlankCount).fill(0),
  ]

  const rows = Array(weekCount)
    .fill(0)
    .map((_, i) => [...allDates].splice(i * 7, 7))

  return rows
}
