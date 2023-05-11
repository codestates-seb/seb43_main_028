// import Icon from '../components/common/Icon'
import { useState } from 'react'
import styles from './HistoryList.module.scss'
import History from '../components/HistoryList/History'
import Calendar from '../components/HistoryList/Calendar'

interface ItemITF {
  id: number
  snapTime: string
  imageUrl: string
  text: string
}

interface DataITF {
  id: number
  mapImg: string
  createdAt: string
  time: string
  message: string
  walkLogContents: ItemITF[]
}

//  historylist?page=1&size=10&sortBy=questionId
const Data: DataITF[] = [
  {
    id: 0,
    mapImg: 'https://developers.kakaomobility.com/_nuxt/img/img_api4.507c175.png',
    createdAt: '2023-05-01',
    time: '1시간 20분',
    message:
      '한 줄 메시지 입니다. 넘어가면 두 줄이 됩니다. 두 줄에서 3줄로 넘어가지 않습니다. 엄청 길어지면 어떻게 해야할까요? 점점점으로 표시해야겠죠?',
    walkLogContents: [
      {
        id: 10,
        snapTime: '00:41',
        imageUrl:
          'https://mblogthumb-phinf.pstatic.net/MjAxOTEyMzFfNDkg/MDAxNTc3ODAzMTIyNjk5.gzT3yhSvbjo4Ro2V2THLJfxyG3Pug8Tm_N-Z2YB_g1sg.2rAXmG8w1ssQ1RUGPkXKXI-BMUAwnTxBbaMZbP4oqG0g.JPEG.achutube/%EC%98%88%EC%81%9C_%ED%8C%8C%EC%8A%A4%ED%85%94%ED%86%A4_%EB%B0%B0%EA%B2%BD%ED%99%94%EB%A9%B4_2.jpg?type=w2',
        text: '본문 내용입니다. 오늘의 산책은 즐거웠다 날씨가 좋으니 기분이 좋다. 매일 산책 해야지',
      },
      {
        id: 11,
        snapTime: '00:50',
        imageUrl: '',
        text: '다른 문을 열어 따라갈 필요는 없어 넌 너의 길로, 난 나의 길로 음 하루하루마다 색이 달라진 느낌 밝게 빛이 나는 길을 찾아 Im on my way 넌 그냥 믿으면 돼 Im on my way 보이는 그대로야',
      },
      {
        id: 12,
        snapTime: '01:00',
        imageUrl: '',
        text: '너는 누군가의 dreams come true 제일 좋은 어느 날의 데자뷰 머물고픈 어딘가의 낯선 뷰 Ill be far away',
      },
    ],
  },
  {
    id: 1,
    mapImg: 'https://developers.kakaomobility.com/_nuxt/img/img_api4.507c175.png',
    createdAt: '2023-05-03',
    time: '2시간',
    message:
      'Its our time 우린 달라, 특별한 게 좋아 Oh, what a good time 난 잘 살아, 내 걱정은 낭비야 니가 보낸 DM을 읽고 나서 답이 없는 게 내 답이야(Thats my style)',
    walkLogContents: [
      {
        id: 13,
        snapTime: '00:10',
        imageUrl:
          'https://mblogthumb-phinf.pstatic.net/MjAxNzEyMjhfMjUy/MDAxNTE0Mzk2Nzk1OTIw.PWl_nBOVPzqVDvRQafLkGHJoMdBIlugmp04UO-2HA3wg.T4FPxQ83QA7qHRuj_9p4sjS-zpv_fv7kW1qQN0Nyeh8g.JPEG.ehaus12/%EC%95%84%EC%9D%B4%ED%8F%B08_%EB%B0%B0%EA%B2%BD%ED%99%94%EB%A9%B4_%284%29.JPG?type=w800',
        text: 'OOTD 하나까지 완전 우리답지 My favorite things 그런 것들엔 좀 점수를 매기지 마 난 생겨 먹은 대로 사는 애야 뭘 더 바래? Thats my style',
      },
      {
        id: 14,
        snapTime: '00:50',
        imageUrl: 'https://t1.daumcdn.net/cfile/tistory/2616FD35574F9F522C',
        text: '우리만의 자유로운 nineteens kitsch 지금까지 한 적 없는 custom fit 올려 대는 나의 feed엔 like it 홀린 듯이 눌러 모두 다 like it',
      },
      {
        id: 15,
        snapTime: '01:00',
        imageUrl: '',
        text: '내가 추는 춤을 다들 따라 춰 매일 너의 알고리즘에 난 떠 겉잡을 수 없이 올라 미친 score 그 누구도 예상 못 할 nineteens kitsch',
      },
    ],
  },
  {
    id: 2,
    mapImg: 'https://developers.kakaomobility.com/_nuxt/img/img_api4.507c175.png',
    createdAt: '2023-05-11',
    time: '2시간',
    message:
      'Its our time 우린 달라, 특별한 게 좋아 Oh, what a good time 난 잘 살아, 내 걱정은 낭비야 니가 보낸 DM을 읽고 나서 답이 없는 게 내 답이야(Thats my style)',
    walkLogContents: [
      {
        id: 16,
        snapTime: '00:10',
        imageUrl:
          'https://mblogthumb-phinf.pstatic.net/MjAxODEwMTJfMjgz/MDAxNTM5MjcwNzk5NDMz._RguaLGCU8YJg8-bjtjSYl64y6hZ8Twk0bn_Q3436iog.FXXHZM-E9K8AKuzoum4zbzL3-3oMnMLWSVRRV_IoGMwg.PNG.mentorkh/%EC%98%88%EC%81%9C%ED%95%98%EB%8A%98_%EA%B0%90%EC%84%B1%EC%9D%B4%EB%AF%B8%EC%A7%80_%EB%B0%B0%EA%B2%BD%ED%99%94%EB%A9%B4__%281%29.png?type=w800',
        text: 'OOTD 하나까지 완전 우리답지 My favorite things 그런 것들엔 좀 점수를 매기지 마 난 생겨 먹은 대로 사는 애야 뭘 더 바래? Thats my style',
      },
      {
        id: 17,
        snapTime: '00:50',
        imageUrl:
          'data:image/jpeg;base64,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',
        text: '우리만의 자유로운 nineteens kitsch 지금까지 한 적 없는 custom fit 올려 대는 나의 feed엔 like it 홀린 듯이 눌러 모두 다 like it',
      },
      {
        id: 18,
        snapTime: '01:00',
        imageUrl: 'https://i.pinimg.com/originals/d2/11/77/d21177861d4c3ee30af8ee649a033b91.jpg',
        text: '내가 추는 춤을 다들 따라 춰 매일 너의 알고리즘에 난 떠 겉잡을 수 없이 올라 미친 score 그 누구도 예상 못 할 nineteens kitsch',
      },
    ],
  },
]

export default function HistoryList() {
  const [calendar, setCalendar] = useState(false)
  const [data, setData] = useState<DataITF[]>(Data)

  const handleCalendar = () => {
    setCalendar(!calendar)
  }

  return (
    <div className={styles.container}>
      <div className={styles.toggleBox}>
        <button
          type='button'
          className={calendar ? styles.btn : styles.clickedBtn}
          onClick={handleCalendar}
        >
          최신순 보기
        </button>
        <button
          type='button'
          className={calendar ? styles.clickedBtn : styles.btn}
          onClick={handleCalendar}
        >
          월별 보기
        </button>
      </div>
      {calendar && <Calendar data={data[0].createdAt} />}
      {Data.map(item => {
        return <History key={item.id} data={item} />
      })}
    </div>
  )
}
