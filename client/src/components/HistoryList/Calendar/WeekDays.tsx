import styles from './WeekDays.module.scss'

export default function WeekDays() {
  const weekdays = ['일', '월', '화', '수', '목', '금', '토']
  return (
    <thead>
      <tr>
        {weekdays.map(weekday => {
          let day
          if (weekday === '일') {
            day = 'sunday'
          } else if (weekday === '토') {
            day = 'saturday'
          } else {
            day = 'day'
          }
          return (
            <td key={weekday} className={styles[day]}>
              {weekday}
            </td>
          )
        })}
      </tr>
    </thead>
  )
}
