import { getAllPublicWalkLogs } from '../apis/walkLog'
import styles from './Feed.module.scss'

export default function Feed() {
  const feeds = getAllPublicWalkLogs()
  return <div>Feed</div>
}
