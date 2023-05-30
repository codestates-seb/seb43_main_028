import { useState } from 'react'
import styles from './Timer.module.scss'
import { timerFormat } from '../../utils/date'
import useInterval from '../../hooks/useInterval'
import { differenceInSeconds } from '../../utils/date-fns'

type TimerProps = {
  startedDate: Date
}

export default function Timer({ startedDate }: TimerProps) {
  const diff = differenceInSeconds(new Date(), startedDate)
  const [count, setCount] = useState(diff - 5)
  useInterval(() => {
    setCount(count + 1)
  })
  return <div className={styles.timer}>{timerFormat(count)}</div>
}
