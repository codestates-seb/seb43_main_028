import addMonths from 'date-fns/addMonths'
import format from 'date-fns/format'
import getDate from 'date-fns/getDate'
import ko from 'date-fns/locale/ko'
import subMonths from 'date-fns/subMonths'
import getDay from 'date-fns/getDay'
import startOfToday from 'date-fns/startOfToday'
import startOfDay from 'date-fns/startOfDay'
import isEqual from 'date-fns/isEqual'
import endOfMonth from 'date-fns/endOfMonth'
import startOfMonth from 'date-fns/startOfMonth'
import eachDayOfInterval from 'date-fns/eachDayOfInterval'
import getWeeksInMonth from 'date-fns/getWeeksInMonth'
import differenceInSeconds from 'date-fns/differenceInSeconds'

function getWeekRows(date: Date): (0 | Date)[][] {
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

export {
  addMonths,
  subMonths,
  format,
  getDate,
  ko,
  getDay,
  getWeekRows,
  startOfToday,
  startOfDay,
  isEqual,
  differenceInSeconds,
}
