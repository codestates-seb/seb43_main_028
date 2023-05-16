import styles from './EditProfile.module.scss'

type EditProfilePropsType = {
  setIsModalOpened: React.Dispatch<React.SetStateAction<boolean>>
}

function EditProfile({ setIsModalOpened }: EditProfilePropsType) {
  const handleCloseEditProfile = () => {
    setIsModalOpened(false)
  }
  return (
    <div className={styles.editContainer}>
      <div className={styles.titleBox}>
        <div className={styles.editTitle}>프로필 수정하기</div>
        <div className={styles.editBtnBox}>
          <button type='button' className={styles.cancelBtn} onClick={handleCloseEditProfile}>
            취소
          </button>
          <button type='button' className={styles.confirmBtn}>
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
        <div className={styles.currentImg}>현재이미지</div>
        <div className={styles.changeImg}>이미지교체</div>
      </div>
      <div className={styles.editName}>
        <input type='text' placeholder='이름' className={styles.editNameInput} />
      </div>
      <div className={styles.editIntroduction}>
        <textarea placeholder='자기소개' className={styles.editIntroductionInput} />
      </div>
    </div>
  )
}

export default EditProfile
