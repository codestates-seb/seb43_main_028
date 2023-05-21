import { useCallback, useRef } from 'react'
import { useInfiniteQuery } from '@tanstack/react-query'
import { getAllPublicWalkLogs } from '../apis/walkLog'
import styles from './Feed.module.scss'
import FeedItem from '../components/Feed/FeedItem'

type FeedsType = [] | null
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

export default function Feed() {
  const { isLoading, isError, data, isFetchingNextPage, hasNextPage, fetchNextPage } =
    useInfiniteQuery({
      queryKey: ['feeds'],
      queryFn: ({ pageParam }) => {
        return getAllPublicWalkLogs(pageParam)
      },
      getNextPageParam: lastPage => {
        if (lastPage) {
          const { page, totalPages } = lastPage.pageInfo
          return page < totalPages ? page + 1 : undefined
        }
      },
    })
  const intObserver = useRef<IntersectionObserver>()
  const lastFeedRef = useCallback(
    (feed: HTMLDivElement) => {
      if (isFetchingNextPage) return
      if (intObserver.current) intObserver.current.disconnect()

      intObserver.current = new IntersectionObserver(entries => {
        if (entries[0].isIntersecting && hasNextPage) {
          fetchNextPage()
        }
      })

      if (feed) {
        intObserver.current.observe(feed)
      }
    },
    [isFetchingNextPage, fetchNextPage, hasNextPage]
  )

  if (isLoading) return <h1>Loading...</h1>
  if (isError) return <h1>Fail...</h1>

  const body = data?.pages.map(page => {
    return page?.data.map((feed: FeedType, index) => {
      if (page?.data.length === index + 1) {
        return <FeedItem key={feed.walkLogId} data={feed} ref={lastFeedRef} />
      }
      return <FeedItem key={feed.walkLogId} data={feed} />
    })
  })

  return <div className={styles.container}>{body}</div>
}
