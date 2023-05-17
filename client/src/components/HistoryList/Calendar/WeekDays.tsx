import styles from './WeekDays.module.scss'

export default function WeekDays() {
  const weekdays = ['일', '월', '화', '수', '목', '금', '토']
  return (
    <thead>
      <tr>
        {weekdays.map(weekday => {
          return (
            <td key={weekday} className={styles[weekday]}>
              {weekday}
            </td>
          )
        })}
      </tr>
    </thead>
  )
}
