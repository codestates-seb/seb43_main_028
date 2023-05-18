import { useEffect, useState } from 'react'
import format from 'date-fns/format'
import { Link } from 'react-router-dom'
import { useAtom } from 'jotai'
import { userAtom, idAtom, isLoginAtom } from '../store/authAtom'
import DropDown from '../components/common/DropDown'
import EditProfile from '../components/MyPage/EditProfile'
import styles from './MyPage.module.scss'
import { UserInfoType, patchUserPrivacySettings } from '../apis/user'
import Icon from '../components/common/Icon'

export default function Mypage() {
  const [isLogin] = useAtom(isLoginAtom)

  const [user, setUser] = useAtom(userAtom)
  const [memberId, setMemberId] = useAtom(idAtom)

  const [userData, setUserData] = useState<UserInfoType | null>(null)
  console.log(userData?.imageUrl)
  const [registeredAt, setRegisteredAt] = useState('')

  const [isModalOpened, setIsModalOpened] = useState(false)
  const handleOpenEditProfile = () => {
    setIsModalOpened(true)
  }
  const handleSetPrivacySettings = async (param: string) => {
    if (userData) {
      const data = new FormData()
      const blob = new Blob([JSON.stringify({ defaultWalkLogPublicSetting: param })], {
        type: 'application/json',
      })

      data.append('patch', blob)
      const res = await patchUserPrivacySettings(`/api/members/${memberId}`, data)
      setUser(res)
    }
  }

  const selectOptions = [
    { id: 1, title: '나만 보기', handleClick: handleSetPrivacySettings, param: 'PRIVATE' },
    { id: 2, title: '전체 공개', handleClick: handleSetPrivacySettings, param: 'PUBLIC' },
  ]

  useEffect(() => {
    setUserData(user)
  }, [user])

  useEffect(() => {
    if (userData) {
      const registeredDate = new Date(userData.createdAt)
      const formattedData = format(registeredDate, 'yyyy-MM-dd')
      setRegisteredAt(formattedData)
    }
  }, [userData])

  if (!isLogin) {
    return <div>로그인해라</div>
  }

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
                <span>{registeredAt}</span>
              </div>
            </div>
            <div className={styles.imageWrapper}>
              {!userData?.imageUrl ? (
                <Icon name='no-profile' size={64} />
              ) : (
                <img
                  className={styles.userProfileImage}
                  src={userData?.imageUrl}
                  alt='your profile'
                />
              )}
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
        <div className={styles.setLinkWrapper}>
          <Link to='/changepassword'>비밀번호 변경하기</Link>
        </div>
        <div className={styles.setLinkWrapper}>
          <button type='button'>회원 탈퇴하기</button>
        </div>
      </div>
    </>
  )
}
