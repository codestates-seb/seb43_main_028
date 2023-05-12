import { useNavigate } from 'react-router-dom'

const useRouter = () => {
  const router = useNavigate()

  return {
    currentPath: window.location.pathname,
    routeTo: (path: string) => router(path),
  }
}

export default useRouter
