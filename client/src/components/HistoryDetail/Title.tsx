import { useState } from 'react'
import { useAtom } from 'jotai'
import Icon from '../common/Icon'
import styles from './Title.module.scss'
import { dateFormat, passedHourMinuteSecondFormat } from '../../utils/date'
import { format } from '../../utils/date-fns'
import DropDown from '../common/DropDown'
import { patchHistoryMessage } from '../../apis/history'
import { isLoginAtom, idAtom } from '../../store/authAtom'

type TitleProps = {
  id: string
  memberId: number
  nickname: string
  startAt: string
  endAt: string
  text: string
  setting: 'PUBLIC' | 'PRIVATE'
}

const OptionsObj = { PUBLIC: '전체 공개', PRIVATE: '나만 보기' }
const OptionsAry = ['전체 공개', '나만 보기']
export default function Title({
  id,
  memberId,
  nickname,
  startAt,
  endAt,
  text,
  setting,
}: TitleProps) {
  const [edit, setEdit] = useState(false)
  const [message, setMessage] = useState(text)
  const [publicSetting, setPublicSetting] = useState(OptionsObj[setting])
  const [isLogin] = useAtom(isLoginAtom)
  const [logInId] = useAtom(idAtom)

  const formattedTime = {
    date: dateFormat(new Date(startAt)),
    timer: passedHourMinuteSecondFormat(new Date(endAt).getTime() - new Date(startAt).getTime()),
    time: `${format(new Date(startAt), 'H:mm')} ~ ${format(new Date(endAt), 'H:mm')}`,
  }

  const filteredOption = OptionsAry.filter(option => option !== publicSetting)
  filteredOption.unshift(publicSetting)
  const dropDownOption = filteredOption.map((option, i) => {
    return {
      id: i,
      title: option,
      handleClick: () => {
        setPublicSetting(option)
      },
    }
  })

  const handleMessageChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setMessage(event.target.value)
  }

  const handleMessageSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    const patchData = JSON.stringify({ message, walkLogPublicSetting: 'PRIVATE' })
    const response = await patchHistoryMessage(id, patchData)
    console.log(response)
    setMessage(response.message)
    setEdit(prev => !prev)
  }

  const editingForm = (
    <form className={styles.formBox} onSubmit={handleMessageSubmit}>
      <label>
        <input
          type='text'
          className={styles.editing}
          value={message}
          onChange={handleMessageChange}
          maxLength={50}
          required
        />

        <div className={styles.iconBox}>
          <Icon name='edit-gray' />
          <button type='submit'>수정 완료</button>
        </div>
      </label>
    </form>
  )

  return (
    <div className={styles.container}>
      {logInId !== memberId && (
        <div>
          <img src='' className={styles.profile} alt='profile' />
          <div className={styles.nickName}>{nickname}</div>
        </div>
      )}
      {isLogin && logInId === memberId && <DropDown options={dropDownOption} />}
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
          {isLogin && logInId === memberId && (
            <div className={styles.iconBox}>
              <Icon name='edit-gray' />
              <button type='button' onClick={() => setEdit(prev => !prev)}>
                한 줄 메시지 수정
              </button>
            </div>
          )}
        </div>
      )}
    </div>
  )
}
