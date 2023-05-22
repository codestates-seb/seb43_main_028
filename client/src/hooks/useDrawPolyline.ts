import { useEffect, useRef, useState } from 'react'

type CoordinateType = {
  lat: number
  lng: number
}[]

const useDrawPolyline = (coordinates: CoordinateType) => {
  const [img, setImg] = useState<string | null>(null)
  const canvasRef = useRef<HTMLCanvasElement | null>(null)

  useEffect(() => {
    if (!canvasRef.current) return

    const canvas = canvasRef.current
    const context = canvas.getContext('2d')

    function drawLines() {
      if (context) {
        context.clearRect(0, 0, canvas.width, canvas.height) // 캔버스 초기화
        context.fillStyle = '#f3f4f6' // 캔버스 배경색 설정
        context.fillRect(0, 0, canvas.width, canvas.height) // 배경색으로 사각형 그리기

        const minLng = Math.min(...coordinates.map(coord => coord.lng))
        const maxLng = Math.max(...coordinates.map(coord => coord.lng))
        const minLat = Math.min(...coordinates.map(coord => coord.lat))
        const maxLat = Math.max(...coordinates.map(coord => coord.lat))

        const pathWidth = canvas.width * 0.6 // 경로의 가로 길이 (캔버스 너비의 60%)
        const pathHeight = canvas.height * 0.6 // 경로의 세로 길이 (캔버스 높이의 60%)
        const offsetX = (canvas.width - pathWidth) / 2 // X 좌표 오프셋
        const offsetY = (canvas.height - pathHeight) / 2 // Y 좌표 오프셋

        context.beginPath()
        for (let i = 0; i < coordinates.length; i += 1) {
          const { lat, lng } = coordinates[i]

          // 좌표를 Canvas 좌표로 변환
          const x = offsetX + ((lng - minLng) / (maxLng - minLng)) * pathWidth
          const y = offsetY + ((lat - minLat) / (maxLat - minLat)) * pathHeight

          if (i === 0) {
            context.moveTo(x, y)
          } else {
            context.lineTo(x, y)
          }
        }
        context.strokeStyle = '#8cff9e'
        context.lineWidth = 3
        context.stroke()
      }
    }

    canvas.width = 64
    canvas.height = 64

    drawLines()

    const polylineImg = canvas.toDataURL('image/png')

    setImg(polylineImg)
  }, [coordinates, canvasRef])

  return { img, canvasRef }
}

export default useDrawPolyline
