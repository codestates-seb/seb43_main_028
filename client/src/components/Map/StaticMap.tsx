import { useState, useEffect, useRef } from 'react'
import { GoogleMapsProvider, useGoogleMap } from '@ubilabs/google-maps-react-hooks'

// 전체 좌표 리스트 받기 (마지막 값이 현재 위치)
// 사이즈, 줌, 중앙 좌표(마지막 값) 받기

type StaticMapProps = {
  mapOptions: google.maps.MapOptions
  coordinates: google.maps.LatLngLiteral[]
  size: { width: string; height: string; borderRadius: string }
}

export default function StaticMap({ mapOptions, coordinates, size }: StaticMapProps) {
  const [mapContainer, setMapContainer] = useState<HTMLDivElement | null>(null)
  const currentCoordinates = coordinates[coordinates.length - 1]

  if (coordinates.length === 0)
    return (
      <div
        style={{
          width: size.width,
          height: size.height,
          borderRadius: size.borderRadius,
          background: '#f3f4f6',
        }}
      >
        프리뷰 지도 로딩 중
      </div>
    )

  return (
    <GoogleMapsProvider
      googleMapsAPIKey={import.meta.env.VITE_GOOGLE_MAPS_API_KEY}
      mapOptions={mapOptions}
      mapContainer={mapContainer}
    >
      <div
        ref={node => setMapContainer(node)}
        style={{ width: size.width, height: size.height, borderRadius: size.borderRadius }}
      />
      <Maker coordinates={currentCoordinates} />
      <Polyline coordinates={coordinates} />
    </GoogleMapsProvider>
  )
}

type MakerProps = {
  coordinates: google.maps.LatLngLiteral
}

function Maker({ coordinates }: MakerProps) {
  const map = useGoogleMap()
  const makerRef = useRef<google.maps.Marker | null>(null)

  useEffect(() => {
    if (map) {
      makerRef.current = new google.maps.Marker({ map, position: coordinates })
      map.panTo(coordinates)
    }
  }, [map, coordinates])

  return null
}

type PolylineProps = {
  coordinates: google.maps.LatLngLiteral[]
}

function Polyline({ coordinates }: PolylineProps) {
  const map = useGoogleMap()
  const polylineRef = useRef<google.maps.Polyline | null>(null)

  useEffect(() => {
    if (map) {
      polylineRef.current = new google.maps.Polyline({
        map,
        path: coordinates,
        strokeColor: '#8cff9e',
        strokeOpacity: 1.0,
        strokeWeight: 6,
      })
    }
  }, [map, coordinates])

  return null
}
