import { useState } from 'react'
import styles from './HistoryList.module.scss'
import History from '../components/HistoryList/History'
import Calendar, { DataType } from '../components/HistoryList/Calendar/Calendar'
import Toggle from '../components/HistoryList/Toggle'

const dummy = {
  pageinfo: {
    page: 1,
    size: 10,
    totalElements: 3,
    totalPages: 1,
  },
  data: [
    {
      id: 0,
      mapImg: 'https://developers.kakaomobility.com/_nuxt/img/img_api4.507c175.png',
      startAt: '2023-05-01T18:55:01.228Z',
      endAt: '2023-05-01T18:55:50.228Z',
      message:
        '한 줄 메시지 입니다. 넘어가면 두 줄이 됩니다. 두 줄에서 3줄로 넘어가지 않습니다. 엄청 길어지면 어떻게 해야할까요? 점점점으로 표시해야겠죠?',
      walkLogContents: [
        {
          id: 10,
          createdAt: '2023-05-01T18:55:05.228Z',
          imageUrl:
            'https://mblogthumb-phinf.pstatic.net/MjAxOTEyMzFfNDkg/MDAxNTc3ODAzMTIyNjk5.gzT3yhSvbjo4Ro2V2THLJfxyG3Pug8Tm_N-Z2YB_g1sg.2rAXmG8w1ssQ1RUGPkXKXI-BMUAwnTxBbaMZbP4oqG0g.JPEG.achutube/%EC%98%88%EC%81%9C_%ED%8C%8C%EC%8A%A4%ED%85%94%ED%86%A4_%EB%B0%B0%EA%B2%BD%ED%99%94%EB%A9%B4_2.jpg?type=w2',
          text: '본문 내용입니다. 오늘의 산책은 즐거웠다 날씨가 좋으니 기분이 좋다. 매일 산책 해야지',
        },
        {
          id: 11,
          createdAt: '2023-05-01T18:55:15.228Z',
          imageUrl: '',
          text: '다른 문을 열어 따라갈 필요는 없어 넌 너의 길로, 난 나의 길로 음 하루하루마다 색이 달라진 느낌 밝게 빛이 나는 길을 찾아 Im on my way 넌 그냥 믿으면 돼 Im on my way 보이는 그대로야',
        },
        {
          id: 12,
          createdAt: '2023-05-01T18:55:45.228Z',
          imageUrl: '',
          text: '너는 누군가의 dreams come true 제일 좋은 어느 날의 데자뷰 머물고픈 어딘가의 낯선 뷰 Ill be far away Thats my Life is 아름다운 갤럭시 Be a writer 장르로는 판타지 내일 내게 열리는 건 big, big 스테이지 So that is who I am',
        },
      ],
    },
    {
      id: 1,
      mapImg: 'https://developers.kakaomobility.com/_nuxt/img/img_api4.507c175.png',
      startAt: '2023-05-05T16:15:19.228Z',
      endAt: '2023-05-05T18:10:19.228Z',
      message:
        'Its our time 우린 달라, 특별한 게 좋아 Oh, what a good time 난 잘 살아, 내 걱정은 낭비야 니가 보낸 DM을 읽고 나서 답이 없는 게 내 답이야(Thats my style)',
      walkLogContents: [
        {
          id: 13,
          createdAt: '2023-05-05T17:15:20.228Z',
          imageUrl:
            'https://mblogthumb-phinf.pstatic.net/MjAxNzEyMjhfMjUy/MDAxNTE0Mzk2Nzk1OTIw.PWl_nBOVPzqVDvRQafLkGHJoMdBIlugmp04UO-2HA3wg.T4FPxQ83QA7qHRuj_9p4sjS-zpv_fv7kW1qQN0Nyeh8g.JPEG.ehaus12/%EC%95%84%EC%9D%B4%ED%8F%B08_%EB%B0%B0%EA%B2%BD%ED%99%94%EB%A9%B4_%284%29.JPG?type=w800',
          text: 'OOTD 하나까지 완전 우리답지 My favorite things 그런 것들엔 좀 점수를 매기지 마 난 생겨 먹은 대로 사는 애야 뭘 더 바래? Thats my style',
        },
        {
          id: 14,
          createdAt: '2023-05-05T17:20:20.228Z',
          imageUrl: 'https://t1.daumcdn.net/cfile/tistory/2616FD35574F9F522C',
          text: '우리만의 자유로운 nineteens kitsch 지금까지 한 적 없는 custom fit 올려 대는 나의 feed엔 like it 홀린 듯이 눌러 모두 다 like it',
        },
        {
          id: 15,
          createdAt: '2023-05-05T17:25:20.228Z',
          imageUrl: '',
          text: '내가 추는 춤을 다들 따라 춰 매일 너의 알고리즘에 난 떠 겉잡을 수 없이 올라 미친 score 그 누구도 예상 못 할 nineteens kitsch',
        },
      ],
    },
    {
      id: 2,
      mapImg: 'https://developers.kakaomobility.com/_nuxt/img/img_api4.507c175.png',
      startAt: '2023-05-16T10:55:19.228Z',
      endAt: '2023-05-16T13:15:20.228Z',
      message: '너가 파티에선 주인공이지 (Feat. 하곤)',
      walkLogContents: [
        {
          id: 16,
          createdAt: '2023-05-16T11:55:19.228Z',
          imageUrl:
            'https://mblogthumb-phinf.pstatic.net/MjAxODEwMTJfMjgz/MDAxNTM5MjcwNzk5NDMz._RguaLGCU8YJg8-bjtjSYl64y6hZ8Twk0bn_Q3436iog.FXXHZM-E9K8AKuzoum4zbzL3-3oMnMLWSVRRV_IoGMwg.PNG.mentorkh/%EC%98%88%EC%81%9C%ED%95%98%EB%8A%98_%EA%B0%90%EC%84%B1%EC%9D%B4%EB%AF%B8%EC%A7%80_%EB%B0%B0%EA%B2%BD%ED%99%94%EB%A9%B4__%281%29.png?type=w800',
          text: '너가 파티에선 주인공이지 그걸 망치는 건 내 몫이지 널 데리러 가니 그 순간이 곧 나의 재미 집에 가자 when its night Party 멈춰 time to go out 재미도 엄청 없어보인다 집에 가서 Ill Make you go wild 우리가 여기선 주인공인 것같어',
        },
        {
          id: 17,
          createdAt: '2023-05-16T12:00:19.228Z',
          imageUrl: '',
          text: 'hey I been dreamin bout you bout it we will get slow here we go (oo we can) 둘이서 (oo we can all night long) And I been drinkin bout nobody else you know Bet u know that 우리도 then',
        },
        {
          id: 18,
          createdAt: '2023-05-16T12:55:19.228Z',
          imageUrl: 'https://i.pinimg.com/originals/d2/11/77/d21177861d4c3ee30af8ee649a033b91.jpg',
          text: '내가 어떻게 하는지 알면 watch yo tone babe (get stoned get roamed get humped get hmm) or do I look kinda boring 내가 볼땐 다 이길 수가 있거든 lemme show yo 일단 니네 연락들은 hold it down 우린 금요일 밤부터 phone을 안 켜두니까 Ye like everytime we gon do it like this shit is day job forkin 맨날',
        },
      ],
    },
  ],
}

export default function HistoryList() {
  const [calendar, setCalendar] = useState<boolean>(false)
  const [data, setData] = useState<DataType[]>(dummy.data)

  const handleCalendar = () => {
    setCalendar(!calendar)
  }

  return (
    <div>
      <Toggle handleCalendar={handleCalendar} calendar={calendar} />
      {calendar && <Calendar data={data} />}
      <ul className={styles.historyList}>
        {data.map(item => {
          return <History key={item.id} data={item} />
        })}
      </ul>
    </div>
  )
}
