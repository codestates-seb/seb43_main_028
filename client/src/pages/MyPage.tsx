import { useEffect, useState } from 'react'
import format from 'date-fns/format'
import { Link } from 'react-router-dom'
import { useAtom } from 'jotai'
import { userAtom, idAtom, isLoginAtom } from '../store/authAtom'
import DropDown from '../components/common/DropDown'
import EditProfile from '../components/MyPage/EditProfile'
import styles from './MyPage.module.scss'
import { UserInfoType, patchUserPrivacySettings, unregisterUser } from '../apis/user'
import Icon from '../components/common/Icon'
import Modal from '../components/common/Modal'
import { removeRefreshTokenFromLocalStorage } from '../utils/refreshTokenHandler'
import useRouter from '../hooks/useRouter'

type ModalOption = {
  title: string
  unregisterFn: () => void
}

export default function Mypage() {
  const [isLogin, setIsLogin] = useAtom(isLoginAtom)

  const [user, setUser] = useAtom(userAtom)
  const [memberId] = useAtom(idAtom)

  const [userData, setUserData] = useState<UserInfoType | null>(null)
  const [registeredAt, setRegisteredAt] = useState('')
  const [isModalOpened, setIsModalOpened] = useState(false)
  const [isUnregisterModalOpened, setIsUnregisterModalOpened] = useState(false)
  const [unregisterModalOption, setUnregisterModalOption] = useState<ModalOption>({
    title: '',
    unregisterFn: () => {},
  })

  const { routeTo } = useRouter()

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

  const modalData = {
    title: unregisterModalOption.title,
    options: [
      {
        label: '확인',
        handleClick: unregisterModalOption.unregisterFn,

        id: 0,
      },
      {
        label: '취소',
        handleClick: () => setIsUnregisterModalOpened(prev => !prev),
        id: 1,
      },
    ],
  }

  const handleUnregister = async () => {
    const res = await unregisterUser(`/api/members/${memberId}`)
    if (res === 'success') {
      removeRefreshTokenFromLocalStorage()
      setIsLogin(false)
    }
  }

  const handleUnregisterModal = () => {
    setIsUnregisterModalOpened(prev => !prev)
    setUnregisterModalOption({
      title: '회원 탈퇴',
      unregisterFn: () => {
        handleUnregister()
        setIsUnregisterModalOpened(prev => !prev)
      },
    })
  }

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
    routeTo('/signin')
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
          <button type='button' onClick={handleUnregisterModal}>
            회원 탈퇴하기
          </button>
        </div>
      </div>
      {isUnregisterModalOpened && (
        <Modal modalData={modalData} onClose={() => setIsUnregisterModalOpened(prev => !prev)} />
      )}
    </>
  )
}
