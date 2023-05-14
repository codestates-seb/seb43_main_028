export default function getHourMinuteDiff(timeDiff: number) {
  const aMinute = 1000 * 60
  const anHour = 1000 * 60 * 60

  if (timeDiff >= anHour) {
    const hour = Math.floor(timeDiff / anHour)
    const rest = timeDiff % anHour
    const minute = Math.floor(rest / aMinute) ? `${Math.floor(rest / aMinute)} 분` : ''
    return `${hour} 시간 ${minute} `
  }
  if (timeDiff >= aMinute) {
    const minute = Math.floor(timeDiff / aMinute)
    return `${minute} 분`
  }

  return '0분'
}
