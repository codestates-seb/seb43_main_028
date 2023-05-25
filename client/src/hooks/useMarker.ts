import { useEffect, useRef } from 'react'

type UseMarkerType = {
  map: google.maps.Map | null
  position: google.maps.LatLngLiteral | null
  icon: {
    path: google.maps.SymbolPath
    scale: number
    fillColor: string
    fillOpacity: number
    strokeWeight: number
    strokeColor: string
  }
}

export default function useMarker({ map, position, icon }: UseMarkerType) {
  const markerRef = useRef<google.maps.Marker | null>(null)

  if (markerRef.current) {
    markerRef.current.setPosition(position)
  }

  useEffect(() => {
    if (!map) return

    markerRef.current = new google.maps.Marker({
      map,
      position,
      icon,
    })

    position && map.panTo(position)
  }, [map])
}
