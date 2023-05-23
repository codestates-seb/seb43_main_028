import { useState, Ref, forwardRef } from 'react'
import { Link } from 'react-router-dom'
import { getTimeAgo } from '../../utils/date'
import styles from './FeedItem.module.scss'
import Icon from '../common/Icon'
import { WalkLogContentsDataType } from '../../types/History'
import SnapItem from './SnapItem'

type FeedType = {
  walkLogId: number | null
  mapImage: string | null
  nickname: string | null
  profileImage: string | undefined
  startedAt: string | number | Date
  walkLogContents: []
  endAt: string | number | Date
  message: string | null
}

type FeedItemProps = {
  data: FeedType
}

export default forwardRef(function FeedItem({ data }: FeedItemProps, ref: Ref<HTMLDivElement>) {
  const [moreContent, setMore] = useState(false)
  const { nickname, profileImage, walkLogId, startedAt, message, walkLogContents } = data

  const handleMore = () => {
    setMore(true)
  }

  const body = (
    <li className={styles.container}>
      <div className={styles.profileImageBox}>
        {profileImage ? (
          <img src={profileImage} className={styles.profileImage} alt='프로필' />
        ) : (
          <div className={styles.profileImage}>
            <Icon name='no-profile' size={64} />
          </div>
        )}

        <div>
          <div>{nickname}</div>
          <div className={styles.itemInfoBox}>
            <p className={styles.date}>{getTimeAgo(new Date(startedAt))}</p>
            <div className={styles.region}>서울시 동대문구</div>
          </div>
        </div>
      </div>
      <p className={styles.message}>{message}</p>
      <ul className={styles.walkLogContentList}>
        {walkLogContents.map((item: WalkLogContentsDataType, index) => {
          if (!moreContent && index > 0) return null
          return <SnapItem key={item.walkLogContentId} item={item} />
        })}
      </ul>
      {!moreContent && walkLogContents.length > 1 && (
        <button type='button' className={styles.moreBtn} onClick={handleMore}>
          <Icon name='three-dot' size={24} /> {walkLogContents.length - 1} more
        </button>
      )}
      <div className={styles.linkBox}>
        {/* <Icon name='search-light' size={24} /> */}
        <Link to={`/history/${walkLogId}`} className={styles.detailBtn}>
          보러 가기
        </Link>
      </div>
    </li>
  )

  const feed = ref ? (
    <div className={styles.containerBox} ref={ref}>
      {body}
    </div>
  ) : (
    <div className={styles.containerBox}>{body}</div>
  )
  return feed
})
