export type ModalOption = {
  title: string
  deleteFn: () => void
}

export type HistoryListDataType = {
  walkLogId: number
  mapImage: string
  startedAt: string
  endAt: string
  message: string
  walkLogContents: WalkLogContentsDataType[]
}

export type WalkLogContentsDataType = {
  createdAt: string
  imageUrl: string | null
  text: string | null
  walkLogContentId: string
}

export type HistoryListPageInfoType = {
  page: number
  size: number
  totalElements: number
  totalPages: number
}
