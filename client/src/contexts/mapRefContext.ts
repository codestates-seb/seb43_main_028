import { createContext } from 'react'

type MapRefContextType = ((node: HTMLDivElement | null) => void) | null

const MapRefContext = createContext<MapRefContextType>(null)

export default MapRefContext
