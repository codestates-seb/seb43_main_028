import { useState } from 'react'
import { useParams } from 'react-router-dom'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useAtom } from 'jotai'
import Title from '../components/HistoryDetail/Title'
import styles from './HistoryDetail.module.scss'
import { timerFormat } from '../utils/date'
import { differenceInSeconds } from '../utils/date-fns'
import DetailItem from '../components/HistoryDetail/DetailItem'
import SnapForm from '../components/OnWalk/SnapForm'
import Modal from '../components/common/Modal'
import { deleteHistory, getHistory, patchHistoryItem } from '../apis/history'
import { WalkLogContentsDataType, ModalOption } from '../types/History'
import { isLoginAtom, idAtom } from '../store/authAtom'
import Header from '../components/common/Header'
import useRouter from '../hooks/useRouter'
import HistoryDetailLoading from './loadingPage/HistoryDetailLoading'

export default function HistoryDetail() {
  const [edit, setEdit] = useState<boolean>(false)
  const [editId, setEditId] = useState<string>()
  const [openDeleteModal, setOpenDeleteModal] = useState(false)
  const [deleteModalOption, setDeleteModalOption] = useState<ModalOption>({
    title: '',
    deleteFn: () => {},
  })
  const [isLogin] = useAtom(isLoginAtom)
  const [logInId] = useAtom(idAtom)

  const { id } = useParams()
  const { routeTo } = useRouter()

  const queryClient = useQueryClient()

  const getHistoryQuery = useQuery({
    queryKey: ['history', id],
    queryFn: () => getHistory(id!),
  })

  const handleDeleteHistory = useMutation({
    mutationFn: () => deleteHistory(id!),
    onSuccess: () => {
      queryClient.invalidateQueries(['history', id])
      routeTo('/history')
    },
  })

  const patchHistoryItemMutation = useMutation<
    unknown,
    unknown,
    { contentId: string; formData: FormData },
    unknown
  >({
    mutationFn: ({ contentId, formData }) => patchHistoryItem(id!, contentId, formData),
    onSuccess: () => {
      queryClient.invalidateQueries(['history', id])
      setEdit(prev => !prev)
    },
  })

  const editHistoryItem = (contentId: string, formData: FormData) => {
    patchHistoryItemMutation.mutate({ contentId, formData })
  }

  if (getHistoryQuery.isLoading) return <HistoryDetailLoading />
  if (getHistoryQuery.error) return <h1>Sorry, can not access to the page</h1>

  const {
    createdAt,
    endAt,
    memberId,
    message,
    nickname,
    profileImage,
    walkLogContents,
    walkLogId,
    walkLogPublicSetting,
  } = getHistoryQuery.data

  const handleEdit = () => {
    setEdit(prev => !prev)
  }

  const handleHistoryDeleteModal = () => {
    setOpenDeleteModal(prev => !prev)
    setDeleteModalOption({
      title: '걷기',
      deleteFn: () => {
        handleDeleteHistory.mutate()
        setOpenDeleteModal(prev => !prev)
      },
    })
  }

  const modalData = {
    title: `${deleteModalOption.title} 기록을 삭제하시겠습니까?`,
    options: [
      {
        label: '삭제하기',
        handleClick: deleteModalOption.deleteFn,
        id: 0,
      },
      {
        label: '취소하기',
        handleClick: () => setOpenDeleteModal(prev => !prev),
        id: 1,
      },
    ],
  }

  const editSnapForm = walkLogContents.map((da: WalkLogContentsDataType) => {
    if (da.walkLogContentId === editId) {
      return (
        <SnapForm
          key={da.walkLogContentId}
          initialImgUrl={da.imageUrl}
          initialText={da.text}
          handleCancel={handleEdit}
          contentId={da.walkLogContentId}
          onSubmit={editHistoryItem}
        />
      )
    }
    return ''
  })

  const detailItems = walkLogContents.map((da: WalkLogContentsDataType) => {
    const snapTimeDiff = differenceInSeconds(new Date(da.createdAt), new Date(createdAt))
    const snapTime = timerFormat(snapTimeDiff)

    return (
      <DetailItem
        key={da.walkLogContentId}
        walkLogId={String(walkLogId)}
        memberId={memberId}
        data={da}
        snapTime={snapTime}
        onEdit={handleEdit}
        setEditId={setEditId}
        setOpenDeleteModal={setOpenDeleteModal}
        setDeleteModalOption={setDeleteModalOption}
      />
    )
  })

  return (
    <div>
      <Header
        hasBackButton
        hasCloseButton={false}
        headerTitle='상세 기록'
        handleCloseFn={() => {}}
      />
      {edit ? (
        editSnapForm
      ) : (
        <div className={styles.container}>
          <Title
            id={walkLogId}
            memberId={memberId}
            nickname={nickname}
            profileImage={profileImage}
            startAt={createdAt}
            endAt={endAt}
            text={message}
            setting={walkLogPublicSetting}
          />
          <div className={styles.map}>지도 재사용 컴포넌츠</div>
          {detailItems}
          {isLogin && logInId === memberId && (
            <div className={styles.deleteBtnBox}>
              <button type='button' className={styles.deleteBtn} onClick={handleHistoryDeleteModal}>
                기록 삭제
              </button>
            </div>
          )}
          {openDeleteModal && (
            <Modal modalData={modalData} onClose={() => setOpenDeleteModal(prev => !prev)} />
          )}
        </div>
      )}
    </div>
  )
}
