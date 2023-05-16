import { useState } from 'react'
import Title from '../components/HistoryDetail/Title'
import styles from './HistoryDetail.module.scss'

import { timerFormat } from '../utils/date'
import { differenceInSeconds } from '../components/HistoryList/Calendar/date-fns'
import DetailItem from '../components/HistoryDetail/DetailItem'
import SnapForm from '../components/OnWalk/SnapForm'
import Modal from '../components/common/Modal'

const dummy = {
  pageinfo: {
    page: 1,
    size: 10,
    totalElements: 3,
    totalPages: 1,
  },
  data: {
    walkLogId: '1',
    startAt: '2023-05-11T14:48:27.98596',
    endAt: '2023-05-11T18:00:00.983845',
    message:
      '너가 파티에선 주인공이지 (Feat. 하곤) 그걸 망치는 건 내 몫이지 널 데리러 가니 그 순간이 곧 나의 재미 집에 가자 when its night Party 멈춰 time to go out 재미도 엄청 없어보인다 집에 가서 I will Make you go wild 우리가 여기선 주인공인 것같어',
    walkLogPublicSetting: '나만 보기', // 전체 공개, 나만 보기 2개 중 1개 (추후 친구 공개 등 확장성 고려 필요)
    coordinates: [
      // 이게 뭔지 ?? 필요 없을 것 같은데 ..? region 확인을 위한 우편번호 필요
      {
        coordinateId: 1,
        lat: 1.21242,
        lng: 2.32424,
        createdAt: '2023-05-11T14:48:27.983845',
      },
    ],
    walkLogContents: [
      {
        id: '10',
        createdAt: '2023-05-11T14:50:27.98596',
        imageUrl:
          'https://mblogthumb-phinf.pstatic.net/MjAxOTEyMzFfNDkg/MDAxNTc3ODAzMTIyNjk5.gzT3yhSvbjo4Ro2V2THLJfxyG3Pug8Tm_N-Z2YB_g1sg.2rAXmG8w1ssQ1RUGPkXKXI-BMUAwnTxBbaMZbP4oqG0g.JPEG.achutube/%EC%98%88%EC%81%9C_%ED%8C%8C%EC%8A%A4%ED%85%94%ED%86%A4_%EB%B0%B0%EA%B2%BD%ED%99%94%EB%A9%B4_2.jpg?type=w2',
        text: '너가 파티에선 주인공이지 그걸 망치는 건 내 몫이지 널 데리러 가니 그 순간이 곧 나의 재미 집에 가자 when its night Party 멈춰 time to go out 재미도 엄청 없어보인다 집에 가서 I will Make you go wild 우리가 여기선 주인공인 것같어',
      },
      {
        id: '11',
        createdAt: '2023-05-11T15:30:27.93333',
        imageUrl: '',
        text: 'hey I been dream in bout you bout it we will get slow here we go (oo we can) 둘이서 (oo we can all night long) And I been drinkin bout nobody else you know Bet u know that 우리도 then',
      },
      {
        id: '12',
        createdAt: '2023-05-11T17:00:05.12345',
        imageUrl: '',
        text: '내가 어떻게 하는지 알면 watch yo tone babe (get stoned get roamed get humped get hmm) or do I look kinda boring 내가 볼땐 다 이길 수가 있거든 lemme show yo 일단 니네 연락들은 hold it down 우린 금요일 밤부터 phone을 안 켜두니까 Ye like everytime we gon do it like this shit is day job forkin 맨날 so 늦었어도 일단은 해 It got to be a coast we funk n do it baked 내 맨 밑에 goes',
      },
    ],
  },
}

export default function HistoryDetail() {
  const [data, setData] = useState(dummy.data)
  const [edit, setEdit] = useState<boolean>(false)
  const [editId, setEditId] = useState<string>()
  const [deleteModal, setDeleteModal] = useState<boolean>(false)

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

  const editSnapForm = data.walkLogContents.map(da => {
    if (da.id === editId) {
      const initialValue = { imgUrl: da.imageUrl, text: da.text }
      return <SnapForm key={da.id} initialValue={initialValue} handleCancel={handleEdit} />
    }
    return ''
  })

  const detailItems = data.walkLogContents.map(da => {
    const snapTimeDiff = differenceInSeconds(new Date(da.createdAt), new Date(data.startAt))
    const snapTime = timerFormat(snapTimeDiff)

    return (
      <DetailItem
        key={da.id}
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
            startAt={data.startAt}
            endAt={data.endAt}
            message={data.message}
            publicSetting={data.walkLogPublicSetting}
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
