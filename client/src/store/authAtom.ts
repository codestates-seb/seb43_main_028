import { atom } from 'jotai'

type UserAtomType = {
  defaultWalkLogPublicSetting: 'PRIVATE' | 'PUBLIC'
  email: string
  imageUrl: string
  introduction: string
  memberId: number
  nickname: string
  createdAt: string
  totalWalkLog: number
  totalWalkLogContent: number
}
// 유저 정보
export const userAtom = atom<UserAtomType>({
  defaultWalkLogPublicSetting: 'PRIVATE',
  email: '',
  imageUrl: '',
  introduction: '',
  memberId: 0,
  nickname: '',
  createdAt: '',
  totalWalkLog: 0,
  totalWalkLogContent: 0,
})

// member id
export const idAtom = atom<number>(0)

// 로그인 여부
export const isLoginAtom = atom<boolean>(false)
