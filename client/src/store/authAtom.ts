import { atom } from 'jotai'

export type UserInfoAtomType = {
  createdAt: string
  email: string
  memberId: number
  nickname: string
  introduction: string | null
  imageUrl: string | null
  totalWalkLog: number
  totalWalkLogContent: number
  recordingWalkLogId: number | null
  defaultWalkLogPublicSetting: 'PRIVATE' | 'PUBLIC'
}

export const userInfoAtom = atom<UserInfoAtomType | null>(null)
