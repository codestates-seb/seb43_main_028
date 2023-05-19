import { useEffect, useRef } from 'react'

type UsePolylineType = {
  map: google.maps.Map | null
  path: google.maps.LatLngLiteral[] | null
}

const defaultPolylineOptions: google.maps.PolylineOptions = {
  strokeColor: '#8cff9e',
  strokeOpacity: 1.0,
  strokeWeight: 6,
}

export default function usePolyline({ map, path }: UsePolylineType) {
  const polylineRef = useRef<google.maps.Polyline | null>(null)

  useEffect(() => {
    if (map) {
      polylineRef.current = new google.maps.Polyline({
        map,
        path,
        ...defaultPolylineOptions,
      })
    }
  }, [map, path])
}
