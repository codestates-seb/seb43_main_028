import { useState } from 'react'
import { useAtom } from 'jotai'
import styles from './EditProfile.module.scss'
import ProfileImgInput from './ProfileImgInput'
import { idAtom, userAtom } from '../../store/authAtom'
import { patchUserProfile } from '../../apis/user'

type EditProfilePropsType = {
  setIsModalOpened: React.Dispatch<React.SetStateAction<boolean>>
}

function EditProfile({ setIsModalOpened }: EditProfilePropsType) {
  const [memberId] = useAtom(idAtom)
  const [, setUser] = useAtom(userAtom)

  const handleCloseEditProfile = () => {
    setIsModalOpened(false)
  }

  const EditedProfileSubmitHandler = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    const formData = new FormData(event.currentTarget)
    const nickname = formData.get('nickname')
    const introduction = formData.get('introduction')
    const image = formData.get('image')

    const data = new FormData()
    const blob = new Blob([JSON.stringify({ nickname, introduction })], {
      type: 'application/json',
    })

    data.append('patch', blob)
    if (image) data.append('profileImage', image)

    if (memberId) {
      const res = await patchUserProfile(`/api/members/${memberId}`, data)
      setUser(res)
      // 전역 상태에 userInfoRes 저장
      setIsModalOpened(false)
      return
    }
    alert('프로필 수정에 실패했습니다.')
  }

  return (
    <form
      className={styles.editFormContainer}
      onSubmit={e => {
        EditedProfileSubmitHandler(e)
      }}
    >
      <div className={styles.titleBox}>
        <div className={styles.editTitle}>프로필 수정하기</div>
        <div className={styles.editBtnBox}>
          <button type='button' className={styles.cancelBtn} onClick={handleCloseEditProfile}>
            취소
          </button>
          <button type='submit' className={styles.confirmBtn}>
            완료
          </button>
        </div>
      </div>
      <div className={styles.editMessage}>
        이름은 공백없이 12자 이하,
        <br />
        자기소개는 60자 이하로 입력가능합니다.
      </div>
      <div className={styles.editProfileImgBox}>
        <ProfileImgInput />
      </div>
      <div className={styles.editName}>
        <input type='text' placeholder='이름' name='nickname' className={styles.editNameInput} />
      </div>
      <div className={styles.editIntroduction}>
        <textarea
          placeholder='자기소개'
          name='introduction'
          className={styles.editIntroductionInput}
        />
      </div>
    </form>
  )
}

export default EditProfile
