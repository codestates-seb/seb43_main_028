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

type ModalOption = {
  title: string
  unregisterFn: () => void
}

const koOptions: ['전체 공개', '나만 보기'] = ['전체 공개', '나만 보기']
const engOptionsObj = { PUBLIC: koOptions[0], PRIVATE: koOptions[1] }

export default function Mypage() {
  const [isLogin, setIsLogin] = useAtom(isLoginAtom)

  const [user, setUser] = useAtom(userAtom)
  const [memberId] = useAtom(idAtom)

  // const [userData, setUserData] = useState<UserInfoType | null>(null)
  const [registeredAt, setRegisteredAt] = useState('')
  const [isModalOpened, setIsModalOpened] = useState(false)
  const [isUnregisterModalOpened, setIsUnregisterModalOpened] = useState(false)
  const [unregisterModalOption, setUnregisterModalOption] = useState<ModalOption>({
    title: '',
    unregisterFn: () => {},
  })
  const [publicSetting, setPublicSetting] = useState(
    engOptionsObj[user.defaultWalkLogPublicSetting as keyof typeof engOptionsObj]
  )

  const filteredOption = koOptions.filter(option => option !== publicSetting)
  filteredOption.unshift(publicSetting)

  const dropDownOption = filteredOption.map((option, i) => {
    const param =
      Object.keys(engOptionsObj).find(
        engOpt => engOptionsObj[engOpt as keyof typeof engOptionsObj] === option
      ) || ''

    return {
      id: i,
      title: option,
      param,
      handleClick: async (paramOpt: string) => {
        if (user) {
          const data = new FormData()
          const blob = new Blob([JSON.stringify({ defaultWalkLogPublicSetting: paramOpt })], {
            type: 'application/json',
          })
          data.append('patch', blob)
          const { resData } = await patchUserPrivacySettings(memberId, data)
          setUser(resData)
        }
      },
    }
  })

  const handleOpenEditProfile = () => {
    setIsModalOpened(true)
  }

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
    const res = await unregisterUser(memberId)
    if (res === 'success') {
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
    if (user) {
      const registeredDate = new Date(user.createdAt)
      const formattedData = format(registeredDate, 'yyyy-MM-dd')
      setRegisteredAt(formattedData)
    }
  }, [user])

  return (
    <>
      {isModalOpened ? <EditProfile setIsModalOpened={setIsModalOpened} /> : null}
      <div className={styles.mypageContainer}>
        <div className={styles.profileBox}>
          <div className={styles.infoBox}>
            <div className={styles.userInfo}>
              <div className={styles.userName}>{user?.nickname}</div>
              <div className={styles.userEmail}>{user?.email}</div>
              <div className={styles.userRegisteredAt}>
                <span>회원 가입일:</span>
                <span>{registeredAt}</span>
              </div>
            </div>
            <div className={styles.imageWrapper}>
              {!user?.imageUrl ? (
                <Icon name='no-profile' size={64} />
              ) : (
                <img className={styles.userProfileImage} src={user?.imageUrl} alt='your profile' />
              )}
            </div>
          </div>
          <div className={styles.userText}>{user?.introduction}</div>
          <button className={styles.editProfileBtn} type='button' onClick={handleOpenEditProfile}>
            프로필 수정하기
          </button>
        </div>
        <div className={styles.configBox}>
          <div className={styles.configTitle}>걷기 기록 공개 설정</div>
          <div className={styles.selectWrapper}>
            <DropDown options={dropDownOption} />
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
