import { Link } from 'react-router-dom'
import { useAtom } from 'jotai'
import { userAtom } from '../store/authAtom'

export default function Home() {
  // userData는 배열의 형태
  const [userData, setUserData] = useAtom(userAtom)
  console.log(userData)

  return (
    <div>
      <Link to='/maptest'>link to maptest</Link>
      <p>1</p>
    </div>
  )
}
