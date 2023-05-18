import { useState } from 'react'
import { useParams } from 'react-router-dom'
import { useQuery } from '@tanstack/react-query'
import Title from '../components/HistoryDetail/Title'
import styles from './HistoryDetail.module.scss'

import { timerFormat } from '../utils/date'
import { differenceInSeconds } from '../utils/date-fns'
import DetailItem from '../components/HistoryDetail/DetailItem'
import SnapForm from '../components/OnWalk/SnapForm'
import Modal from '../components/common/Modal'
import { getHistory } from '../apis/history'
import { WalkLogContentsDataType } from '../types/HistoryDetail'

export default function HistoryDetail() {
  const [edit, setEdit] = useState<boolean>(false)
  const [editId, setEditId] = useState<string>()
  const [deleteModal, setDeleteModal] = useState<boolean>(false)
  const { id } = useParams()
  const getHistoryQuery = useQuery({
    queryKey: ['history', id],
    queryFn: () => {
      if (id) {
        return getHistory(id)
      }
      return console.log('no id')
    },
  })

  if (getHistoryQuery.isLoading) return <h1>Loading...</h1>
  if (getHistoryQuery.error) return <h1>Sorry, can not access to the page</h1>

  const {
    createdAt,
    endAt,
    memberId,
    message,
    nickname,
    walkLogContents,
    walkLogId,
    walkLogPublicSetting,
  } = getHistoryQuery.data

  console.log('queryData', getHistoryQuery.data)

  const handleEdit = () => {
    setEdit(prev => !prev)
  }

  const handleDeleteModal = () => {
    setDeleteModal(prev => !prev)
  }

  const modalData = {
    title: '기록을 삭제하시겠습니까?',
    options: [
      {
        label: '삭제하기',
        handleClick: () => console.log('진짜 삭제'),
        id: 0,
      },
      {
        label: '취소하기',
        handleClick: handleDeleteModal,
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
        data={da}
        snapTime={snapTime}
        onEdit={handleEdit}
        setEditId={setEditId}
      />
    )
  })

  return (
    <div>
      {edit ? (
        editSnapForm
      ) : (
        <div className={styles.container}>
          <Title
            id={walkLogId}
            startAt={createdAt}
            endAt={endAt}
            text={message}
            setting={walkLogPublicSetting}
          />
          <div className={styles.map}>지도 재사용 컴포넌츠</div>
          {detailItems}
          <div className={styles.deleteBtnBox}>
            <button type='button' className={styles.deleteBtn} onClick={handleDeleteModal}>
              기록 삭제
            </button>
          </div>
          {deleteModal && <Modal modalData={modalData} onClose={handleDeleteModal} />}
        </div>
      )}
    </div>
  )
}
