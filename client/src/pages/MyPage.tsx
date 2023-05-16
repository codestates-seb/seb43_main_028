import { useEffect, useState } from 'react'
import { useAtom } from 'jotai'
import { userAtom } from '../store/authAtom'
import DropDown from '../components/common/DropDown'
import EditProfile from '../components/MyPage/EditProfile'
import styles from './MyPage.module.scss'
import { UserInfoType } from '../apis/user'

export default function Mypage() {
  const [user, setUser] = useAtom(userAtom)
  const [userData, setUserData] = useState<UserInfoType | null>(null)

  const [isModalOpened, setIsModalOpened] = useState(false)
  const handleOpenEditProfile = () => {
    setIsModalOpened(true)
  }

  // export type UserInfoType = {
  //   defaultWalkLogPublicSetting: string
  //   email: string
  //   imageUrl: string
  //   introduction: string
  //   memberId: number
  //   nickname: string
  //   totalWalkLog: number
  //   totalWalkLogContent: number
  // }

  const selectOptions = [
    { id: 1, title: '나만 보기' },
    { id: 2, title: '전체 공개' },
  ]

  useEffect(() => {
    setUserData(user)
  }, [user])

  return (
    <>
      {isModalOpened ? <EditProfile setIsModalOpened={setIsModalOpened} /> : null}
      <div className={styles.mypageContainer}>
        <div className={styles.profileBox}>
          <div className={styles.infoBox}>
            <div className={styles.userInfo}>
              <div className={styles.userName}>{userData?.nickname}</div>
              <div className={styles.userEmail}>{userData?.email}</div>
              <div className={styles.userRegisteredAt}>
                <span>회원 가입일:</span>
                <span>2023-05-02</span>
              </div>
            </div>
            <div className={styles.imageWrapper}>
              <img
                className={styles.userProfileImage}
                src={userData?.imageUrl}
                alt='your profile'
              />
            </div>
          </div>
          <div className={styles.userText}>{userData?.introduction}</div>
          <button className={styles.editProfileBtn} type='button' onClick={handleOpenEditProfile}>
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
    </>
  )
}
