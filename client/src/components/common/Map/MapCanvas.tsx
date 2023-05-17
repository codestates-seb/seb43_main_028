import { forwardRef } from 'react'

type MapCanvasProps = {
  style: React.CSSProperties
}

const MapCanvas = forwardRef<HTMLDivElement, MapCanvasProps>((props, ref) => {
  const { style } = props

  return <div ref={ref} style={style} />
})

MapCanvas.displayName = 'MapCanvas'

export default MapCanvas
