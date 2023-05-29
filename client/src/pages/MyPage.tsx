import { useEffect, useState } from 'react'
import format from 'date-fns/format'
import { Link } from 'react-router-dom'
import { useAtom } from 'jotai'
import { UserInfoAtomType, userInfoAtom } from '../store/authAtom'
import DropDown from '../components/common/DropDown'
import EditProfile from '../components/MyPage/EditProfile'
import styles from './MyPage.module.scss'
import { logoutUser, patchUserPrivacySettings, unregisterUser } from '../apis/user'
import Icon from '../components/common/Icon'
import Modal from '../components/common/Modal'
import useRouter from '../hooks/useRouter'
import MyPageLoading from './loadingPage/MyPageLoading'

type UnregisterModalOptionType = {
  title: string
  unregisterFn: () => void
}

type LogoutModalOptionType = {
  title: string
  logoutFn: () => void
}

export default function Mypage() {
  const { routeTo } = useRouter()
  const [userInfo, setUserInfo] = useAtom(userInfoAtom)

  const [registeredAt, setRegisteredAt] = useState('')
  const [isModalOpened, setIsModalOpened] = useState(false)
  const [isUnregisterModalOpened, setIsUnregisterModalOpened] = useState(false)
  const [unregisterModalOption, setUnregisterModalOption] = useState<UnregisterModalOptionType>({
    title: '',
    unregisterFn: () => {},
  })
  const [isLogoutModalOpened, setIsLogoutModalOpened] = useState(false)
  const [logoutModalOption, setLogoutModalOption] = useState<LogoutModalOptionType>({
    title: '',
    logoutFn: () => {},
  })

  const handleChangePublicSetting = async (paramOpt: string) => {
    if (userInfo) {
      const data = new FormData()
      const blob = new Blob([JSON.stringify({ defaultWalkLogPublicSetting: paramOpt })], {
        type: 'application/json',
      })
      data.append('patch', blob)
      const { resData } = await patchUserPrivacySettings(userInfo.memberId, data)
      setUserInfo(resData)
    }
  }

  const handleOpenEditProfile = () => {
    setIsModalOpened(true)
  }

  const unregisterModalData = {
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
    const res = await unregisterUser((userInfo as UserInfoAtomType).memberId)
    if (res === 'success') {
      setUserInfo(null)
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

  const logoutModalData = {
    title: logoutModalOption.title,
    options: [
      {
        label: '확인',
        handleClick: logoutModalOption.logoutFn,

        id: 0,
      },
      {
        label: '취소',
        handleClick: () => setIsLogoutModalOpened(prev => !prev),
        id: 1,
      },
    ],
  }

  const handleLogout = async () => {
    const res = await logoutUser()
    if (res === 'success') {
      setUserInfo(null)
      routeTo('/')
    }
  }

  const handleLogoutModal = () => {
    setIsLogoutModalOpened(prev => !prev)
    setLogoutModalOption({
      title: '로그아웃',
      logoutFn: () => {
        handleLogout()
        setIsLogoutModalOpened(prev => !prev)
      },
    })
  }

  useEffect(() => {
    if (userInfo?.createdAt) {
      const registeredDate = new Date(userInfo.createdAt)
      const formattedData = format(registeredDate, 'yyyy-MM-dd')
      setRegisteredAt(formattedData)
    }
  }, [userInfo])

  if (!userInfo) {
    return (
      <div className={styles.noHistoryBox}>
        <p>
          <strong>내정보</strong>는 로그인 후 이용하실 수 있습니다.
        </p>
        <Link to='/signin' className={styles.linkBtn}>
          로그인 하러가기
        </Link>
        <Link to='/signup' className={styles.linkBtn}>
          회원가입 하러가기
        </Link>
      </div>
    )
  }

  return (
    <>
      {isModalOpened ? <EditProfile setIsModalOpened={setIsModalOpened} /> : null}
      <div className={styles.mypageContainer}>
        <div className={styles.profileBox}>
          <div className={styles.infoBox}>
            <div className={styles.userInfo}>
              <div className={styles.userName}>{userInfo?.nickname}</div>
              <div className={styles.userEmail}>{userInfo?.email}</div>
              <div className={styles.userRegisteredAt}>
                <span>회원 가입일:</span>
                <span>{registeredAt}</span>
              </div>
            </div>
            <div className={styles.imageWrapper}>
              {!userInfo?.imageUrl ? (
                <Icon name='no-profile' size={64} />
              ) : (
                <img
                  className={styles.userProfileImage}
                  src={userInfo?.imageUrl}
                  alt='your profile'
                />
              )}
            </div>
          </div>
          <div className={styles.userText}>{userInfo?.introduction}</div>
          <button className={styles.editProfileBtn} type='button' onClick={handleOpenEditProfile}>
            프로필 수정하기
          </button>
        </div>
        <div className={styles.configBox}>
          <div className={styles.configTitle}>걷기 기록 공개 설정</div>
          <div className={styles.selectWrapper}>
            <DropDown
              currentSetting={(userInfo as UserInfoAtomType).defaultWalkLogPublicSetting}
              onSubmit={handleChangePublicSetting}
            />
          </div>
          <div className={styles.configMessage}>
            전체공개 시 내 걷기 기록이 피드 탭에서 모든 유저에게 보입니다.
          </div>
        </div>
        <div className={styles.setLinkWrapper900}>
          <Link to='/changepassword'>비밀번호 변경하기</Link>
        </div>
        <div className={styles.setLinkWrapper900}>
          <button type='button' onClick={handleLogoutModal}>
            로그아웃
          </button>
        </div>
        <div className={styles.setLinkWrapper600}>
          <button type='button' onClick={handleUnregisterModal}>
            회원 탈퇴하기
          </button>
        </div>
      </div>
      {isUnregisterModalOpened && (
        <Modal
          modalData={unregisterModalData}
          onClose={() => setIsUnregisterModalOpened(prev => !prev)}
        />
      )}
      {isLogoutModalOpened && (
        <Modal modalData={logoutModalData} onClose={() => setIsLogoutModalOpened(prev => !prev)} />
      )}
    </>
  )
}
