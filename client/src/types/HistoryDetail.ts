export type WalkLogContentsDataType = {
  createdAt: string
  imageUrl: string | null
  text: string | null
  walkLogContentId: string
}

export type ModalOption = {
  title: string
  deleteFn: () => void
}
