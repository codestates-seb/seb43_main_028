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
  const [imgFile, setImgFile] = useState<File | undefined>()

  const [memberId] = useAtom(idAtom)
  const [user, setUser] = useAtom(userAtom)
  const [preview, setPreview] = useState<string>(user.imageUrl)

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

    if (nickname === user.nickname) {
      const blob = new Blob([JSON.stringify({ introduction })], {
        type: 'application/json',
      })
      data.append('patch', blob)
    } else {
      const blob = new Blob([JSON.stringify({ nickname, introduction })], {
        type: 'application/json',
      })

      data.append('patch', blob)
    }

    // 기존 프로필 사진을 지우고 텍스트를 수정해서 보낸 경우
    // image === null
    if (image instanceof File && !image.name && !preview) {
      data.append('profileImage', image)
    }

    // 이미지를 수정한 경우
    if (image instanceof File && image.name && preview) {
      data.append('profileImage', image)
    }

    if (memberId) {
      const { status, resData } = await patchUserProfile(memberId, data)
      if (status === 'success' && resData) {
        setUser(resData)
        setIsModalOpened(false)
        return
      }
      if (status === 'nickname-exists') {
        alert('이미 존재하는 닉네임입니다.')
        return
      }
      alert('잠시 후 다시 시도해주세요.')
    }
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
        <ProfileImgInput
          imgFile={imgFile}
          setImgFile={setImgFile}
          profileImage={user.imageUrl ? user.imageUrl : ''}
          preview={preview}
          setPreview={setPreview}
        />
      </div>
      <div className={styles.editName}>
        <input
          type='text'
          placeholder='이름'
          name='nickname'
          className={styles.editNameInput}
          defaultValue={user.nickname}
        />
      </div>
      <div className={styles.editIntroduction}>
        <textarea
          placeholder='자기소개'
          name='introduction'
          className={styles.editIntroductionInput}
          defaultValue={user.introduction}
        />
      </div>
    </form>
  )
}

export default EditProfile
