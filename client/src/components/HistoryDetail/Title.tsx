import { useEffect, useState } from 'react'
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
  profileImage: string
  startAt: string
  endAt: string
  text: string
  setting: 'PUBLIC' | 'PRIVATE'
}

export default function Title({
  id,
  memberId,
  nickname,
  profileImage,
  startAt,
  endAt,
  text,
  setting,
}: TitleProps) {
  const [edit, setEdit] = useState(false)
  const [message, setMessage] = useState(text)
  const [isLogin] = useAtom(isLoginAtom)
  const [logInId] = useAtom(idAtom)
  const formattedTime = {
    date: dateFormat(new Date(startAt)),
    timer: passedHourMinuteSecondFormat(new Date(endAt).getTime() - new Date(startAt).getTime()),
    time: `${format(new Date(startAt), 'H:mm')} ~ ${format(new Date(endAt), 'H:mm')}`,
  }

  const queryClient = useQueryClient()
  const handlePatchHistory = useMutation<
    { message: string; walkLogPublicSetting: 'PUBLIC' | 'PRIVATE' },
    unknown,
    string,
    unknown
  >({
    mutationFn: data => patchHistory(id, data),
    onSuccess: data => {
      queryClient.invalidateQueries(['history', id])
      setMessage(data.message)
    },
  })

  useEffect(() => {
    setMessage(text)
  }, [text])

  const handleMessageSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    const formData = new FormData(event.currentTarget)
    const patchData = JSON.stringify({
      message: formData.get('message'),
      walkLogPublicSetting: setting,
    })
    handlePatchHistory.mutate(patchData)
    setEdit(prev => !prev)
  }

  const handlePublicSettingSubmit = async (selectOption: string) => {
    const patchData = JSON.stringify({
      message,
      walkLogPublicSetting: selectOption,
    })

    handlePatchHistory.mutate(patchData)
  }

  const editingForm = (
    <form className={styles.formBox} onSubmit={handleMessageSubmit}>
      <label>
        <input
          type='text'
          name='message'
          className={styles.editing}
          defaultValue={message}
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
        <div className={styles.profileNicknameBox}>
          {profileImage ? (
            <img src={profileImage} alt='profile' />
          ) : (
            <Icon name='no-profile' size={16} />
          )}
          <div>{nickname}</div>
        </div>
      )}
      {isLogin && logInId === memberId && (
        <DropDown currentSetting={setting} onSubmit={handlePublicSettingSubmit} />
      )}
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
