import { useContext } from 'react'
import MapRefContext from '../contexts/mapRefContext'

const useMapRef = () => {
  const mapRef = useContext(MapRefContext)

  return mapRef
}

export default useMapRef
