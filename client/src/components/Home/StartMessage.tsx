import Spinner from '../../pages/loadingPage/Spinner'
import styles from './StartMessage.module.scss'

export default function StartMessage() {
  const startMessages = [
    { id: 0, text: '오늘 하루를 정리하며 걸어보는 건 어떨까요?' },
    { id: 1, text: '요즘 연락이 뜸했던 친구에게 전화를 걸어보는 건 어떨까요?' },
    { id: 2, text: '내일 하루를 미리 그려보며 걷는 건 어떨까요?' },
    { id: 3, text: '동네 친구를 불러내 함께 걸어보는 건 어떠세요?' },
    { id: 4, text: '시간을 버리듯이 걷다 보면 오히려 많은 걸 얻게 될지도 몰라요.' },
  ]

  const getRandomMessage = () =>
    startMessages[Math.floor(Math.random() * startMessages.length)].text

  return (
    <div className={styles.messageContainer}>
      <Spinner label={getRandomMessage()} hasBg={false} />
    </div>
  )
}
