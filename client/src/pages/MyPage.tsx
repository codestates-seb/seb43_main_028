import DropDown from '../components/common/DropDown'
import styles from './MyPage.module.scss'

export default function Mypage() {
  const selectOptions = [
    { id: 1, title: '나만 보기' },
    { id: 2, title: '전체 공개' },
  ]
  return (
    <div className={styles.mypageContainer}>
      <div className={styles.profileBox}>
        <div className={styles.infoBox}>
          <div className={styles.userInfo}>
            <div className={styles.userName}>하종승</div>
            <div className={styles.userEmail}>aaa@email.com</div>
            <div className={styles.userRegisteredAt}>
              <span>회원 가입일:</span>
              <span>2023-05-02</span>
            </div>
          </div>
          <div className={styles.imageWrapper}>
            <img
              className={styles.userProfileImage}
              src='https://ifh.cc/g/b8pZgt.png'
              alt='your profile'
            />
          </div>
        </div>
        <div className={styles.userText}>
          걷기를 사랑하는 남자 하 종 승 입니다. 걷기를 정말 사랑하는 남자 하 종 승 입니다.
        </div>
        <button className={styles.editProfileBtn} type='button'>
          프로필 수정하기
        </button>
      </div>
      <div className={styles.configBox}>
        <div className={styles.configTitle}>걷기 기록 공개 설정</div>
        <div className={styles.selectWrapper}>
          <DropDown options={selectOptions} />
        </div>
        <div className={styles.configMessage}>
          전체공개 시 내 걷기 기록이 피드 탭에서 모든 유저에게 보입니다.
        </div>
      </div>
      <div className={styles.unregisterWrapper}>
        <div>회원 탈퇴하기</div>
      </div>
    </div>
  )
}
