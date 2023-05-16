import Icon from '../common/Icon'
import styles from './Title.module.scss'
import { dateFormat, passedHourMinuteSecondFormat } from '../../utils/date'
import { format } from '../HistoryList/Calendar/date-fns'

type TitleProps = {
  startAt: string
  endAt: string
  message: string
  publicSetting: string
}

export default function Title({ startAt, endAt, message, publicSetting }: TitleProps) {
  const start = new Date(startAt)
  const end = new Date(endAt)
  const formattedTime = {
    date: dateFormat(start),
    timer: passedHourMinuteSecondFormat(end.getTime() - start.getTime()),
    time: `${format(start, 'H:mm')} ~ ${format(end, 'H:mm')}`,
  }

  return (
    <div className={styles.container}>
      <div>{publicSetting}</div>
      <div className={styles.timeBox}>
        {/* <Icon name='' size={16} /> */}
        <div>{formattedTime.date}</div>
        <Icon name='time-gray' size={16} />
        <div>{formattedTime.timer}</div>
        <div>({formattedTime.time})</div>
      </div>
      <div>{message}</div>
      <div>
        <Icon name='edit-gray' />한 줄 메시지 수정
      </div>
    </div>
  )
}
