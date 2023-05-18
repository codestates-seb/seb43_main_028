import { useState } from 'react'
import Icon from '../common/Icon'
import styles from './Title.module.scss'
import { dateFormat, passedHourMinuteSecondFormat } from '../../utils/date'
import { format } from '../../utils/date-fns'
import DropDown from '../common/DropDown'

type TitleProps = {
  startAt: string
  endAt: string
  message: string
  publicSetting: 'PUBLIC' | 'PRIVATE'
}

const OptionsObj = { PUBLIC: '전체 공개', PRIVATE: '나만 보기' }
const OptionsAry = ['전체 공개', '나만 보기']
export default function Title({ startAt, endAt, message, publicSetting }: TitleProps) {
  const [edit, setEdit] = useState(false)

  const formattedTime = {
    date: dateFormat(new Date(startAt)),
    timer: passedHourMinuteSecondFormat(new Date(endAt).getTime() - new Date(startAt).getTime()),
    time: `${format(new Date(startAt), 'H:mm')} ~ ${format(new Date(endAt), 'H:mm')}`,
  }

  const filter = OptionsAry.filter(o => o !== OptionsObj[publicSetting])
  filter.unshift(OptionsObj[publicSetting])
  const dropDownOption = filter.map((o, i) => {
    return {
      id: i,
      title: o,
      handleClick: () => {
        console.log(`${o}로 공개 설정 변경`)
      },
    }
  })

  const handleMessageEdit = () => {
    setEdit(prev => !prev)
    console.log('edit!')
  }

  const handleMessageSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setEdit(prev => !prev)
    console.log('submit!')
  }

  const editingForm = (
    <form className={styles.formBox} onSubmit={handleMessageSubmit}>
      <label>
        <input type='text' className={styles.editing} defaultValue={message} />
        <div className={styles.iconBox}>
          <Icon name='edit-gray' />
          <button type='submit'>수정 완료</button>
        </div>
      </label>
    </form>
  )

  return (
    <div className={styles.container}>
      <DropDown options={dropDownOption} />
      <div className={styles.timeBox}>
        <div className={styles.iconBox}>
          <Icon name='calendar-gray' size={16} />
          <div>{formattedTime.date}</div>
        </div>
        <div className={styles.iconBox}>
          <Icon name='time-gray' size={16} />
          <div>{formattedTime.timer}</div>
          <div>({formattedTime.time})</div>
        </div>
      </div>
      {edit ? (
        editingForm
      ) : (
        <div>
          <p className={styles.message}>{message}</p>
          <div className={styles.iconBox}>
            <Icon name='edit-gray' />
            <button type='button' onClick={handleMessageEdit}>
              한 줄 메시지 수정
            </button>
          </div>
        </div>
      )}
    </div>
  )
}
