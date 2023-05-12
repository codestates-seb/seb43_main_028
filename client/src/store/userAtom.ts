import { atom } from 'jotai'

// 유저 정보를 담을 atom 생성
const userAtom = atom<object>({
  defaultWalkLogPublicSetting: 'PRIVATE',
  email: null,
  imageUrl: null,
  introduction: null,
  memberId: null,
  nickname: null,
  totalWalkLog: 0,
  totalWalkLogContent: 0,
})

export default userAtom
