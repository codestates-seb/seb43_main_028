import { useEffect, useRef } from 'react'

type UsePolylineType = {
  map: google.maps.Map | null
  path: google.maps.LatLngLiteral[] | null
}

export default function usePolyline({ map, path }: UsePolylineType) {
  const polylineRef = useRef<google.maps.Polyline | null>(null)

  useEffect(() => {
    if (!path || !map) return
    polylineRef.current = new google.maps.Polyline({
      map,
      path,
      strokeColor: '#8cff9e',
      strokeOpacity: 1.0,
      strokeWeight: 6,
    })
  }, [map, path])
}
