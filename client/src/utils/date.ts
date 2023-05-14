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
