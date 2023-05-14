import { atom } from 'jotai'

// 유저 정보
export const userAtom = atom<object>({
  defaultWalkLogPublicSetting: 'PRIVATE',
  email: null,
  imageUrl: null,
  introduction: null,
  memberId: null,
  nickname: null,
  totalWalkLog: 0,
  totalWalkLogContent: 0,
})

// member id
export const idAtom = atom<number>(0)

// 로그인 여부
export const isLoginAtom = atom<boolean>(false)
