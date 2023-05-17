import { useEffect, useRef } from 'react'

type UseMarkerType = {
  map: google.maps.Map | null
  position: google.maps.LatLngLiteral | null
}

export default function useMarker({ map, position }: UseMarkerType) {
  const markerRef = useRef<google.maps.Marker | null>(null)

  useEffect(() => {
    if (!map || markerRef.current) return

    markerRef.current = new google.maps.Marker({
      map,
      position,
      visible: true,
      icon: {
        path: google.maps.SymbolPath.CIRCLE,
        scale: 10,
        fillColor: '#8cff9e',
        fillOpacity: 1,
        strokeWeight: 4,
        strokeColor: 'white',
      },
    })
  }, [map, position])
}
