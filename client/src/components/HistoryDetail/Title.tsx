import { useRef, useState } from 'react'
import { useAtom } from 'jotai'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import Icon from '../common/Icon'
import styles from './Title.module.scss'
import { dateFormat, passedHourMinuteSecondFormat } from '../../utils/date'
import { format } from '../../utils/date-fns'
import DropDown from '../common/DropDown'
import { patchHistory } from '../../apis/history'
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

const koOptions = ['전체 공개', '나만 보기'] as const
const engOptionsObj = { PUBLIC: '전체 공개', PRIVATE: '나만 보기' } as const
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
  const [publicSetting, setPublicSetting] = useState(engOptionsObj[setting])
  const [isLogin] = useAtom(isLoginAtom)
  const [logInId] = useAtom(idAtom)
  const formattedTime = {
    date: dateFormat(new Date(startAt)),
    timer: passedHourMinuteSecondFormat(new Date(endAt).getTime() - new Date(startAt).getTime()),
    time: `${format(new Date(startAt), 'H:mm')} ~ ${format(new Date(endAt), 'H:mm')}`,
  }

  const messageRef = useRef<HTMLInputElement>(null)

  const queryClient = useQueryClient()
  const handlePatchHistory = useMutation<
    { message: string; walkLogPublicSetting: 'PUBLIC' | 'PRIVATE' },
    unknown,
    string,
    unknown
  >({
    mutationFn: data => patchHistory(id, data),
    onSuccess: data => {
      setMessage(data.message)
      queryClient.invalidateQueries(['history', id])
    },
  })

  const filteredOption = koOptions.filter(option => option !== publicSetting)
  filteredOption.unshift(publicSetting)

  const dropDownOption = filteredOption.map((option, i) => {
    const param =
      Object.keys(engOptionsObj).find(
        engOpt => engOptionsObj[engOpt as 'PUBLIC' | 'PRIVATE'] === option
      ) || ''

    return {
      id: i,
      title: option,
      param,
      handleClick: (paramOpt: string) => {
        const patchData = JSON.stringify({
          message,
          walkLogPublicSetting: paramOpt,
        })
        handlePatchHistory.mutate(patchData)
        if (handlePatchHistory.isSuccess) {
          setPublicSetting(option)
        }
      },
    }
  })

  const handleMessageSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    const patchData = JSON.stringify({
      message: messageRef.current ? messageRef.current.value : '',
      walkLogPublicSetting: 'PRIVATE',
    })
    handlePatchHistory.mutate(patchData)
    setEdit(prev => !prev)
  }

  const editingForm = (
    <form className={styles.formBox} onSubmit={handleMessageSubmit}>
      <label>
        <input
          type='text'
          className={styles.editing}
          defaultValue={message}
          ref={messageRef}
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
