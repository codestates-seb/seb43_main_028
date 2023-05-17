import { useState } from 'react'
import Icon from '../common/Icon'
import styles from './Title.module.scss'
import { dateFormat, passedHourMinuteSecondFormat } from '../../utils/date'
import { format } from '../HistoryList/Calendar/date-fns'
import DropDown from '../common/DropDown'

type TitleProps = {
  startAt: string
  endAt: string
  message: string
  publicSetting: 'Public' | 'Private'
}

const OptionsObj = { Public: '전체 공개', Private: '나만 보기' }
const OptionsAry = ['전체 공개', '나만 보기']
export default function Title({ startAt, endAt, message, publicSetting }: TitleProps) {
  const [edit, setEdit] = useState(true)

  const start = new Date(startAt)
  const end = new Date(endAt)
  const formattedTime = {
    date: dateFormat(start),
    timer: passedHourMinuteSecondFormat(end.getTime() - start.getTime()),
    time: `${format(start, 'H:mm')} ~ ${format(end, 'H:mm')}`,
  }

  const filter = OptionsAry.filter(o => o !== OptionsObj[publicSetting])
  filter.unshift(OptionsObj[publicSetting])
  const dropDownOption = filter.map((o, i) => {
    return { id: i, title: o }
  })

  const handleMessageEdit = (event: React.FormEvent<HTMLButtonElement>) => {
    event.preventDefault()
    setEdit(prev => !prev)
    console.log('edit!')
  }

  const handleMessageSubmit = (event: React.FormEvent<HTMLButtonElement>) => {
    event.preventDefault()
    setEdit(prev => !prev)
    console.log('submit!')
  }

  return (
    <div className={styles.container}>
      <DropDown options={dropDownOption} />
      <div className={styles.timeBox}>
        <div className={styles.iconBox}>
          <Icon name='calendar' size={24} />
          <div>{formattedTime.date}</div>
        </div>
        <div className={styles.iconBox}>
          <Icon name='time-gray' size={16} />
          <div>{formattedTime.timer}</div>
          <div>({formattedTime.time})</div>
        </div>
      </div>
      <form className={styles.formBox} onSubmit={e => e.preventDefault()}>
        <textarea
          id='message'
          className={edit ? styles.message : styles.editing}
          defaultValue={message}
          disabled={edit}
        />
        <label htmlFor='message' className={styles.edit}>
          <Icon name='edit-gray' />
          {edit ? (
            <button type='button' onClick={handleMessageEdit}>
              한 줄 메시지 수정
            </button>
          ) : (
            <button type='submit' onClick={handleMessageSubmit}>
              수정 완료
            </button>
          )}
        </label>
      </form>
    </div>
  )
}
