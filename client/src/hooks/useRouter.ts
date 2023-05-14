import { useNavigate, useLocation } from 'react-router-dom'

const useRouter = () => {
  const navigate = useNavigate()
  const { pathname } = useLocation()

  return {
    pathname,
    routeTo: (path: string) => navigate(path),
  }
}

export default useRouter
