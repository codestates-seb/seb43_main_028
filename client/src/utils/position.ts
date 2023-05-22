const R = 6371 // Radius of the earth in km
const deg2rad = (deg: number): number => deg * (Math.PI / 180)

export function getDistanceBetweenPosition(
  location1: google.maps.LatLngLiteral,
  location2: google.maps.LatLngLiteral
) {
  const { lat: lat1, lng: lng1 } = location1
  const { lat: lat2, lng: lng2 } = location2

  const dLat = deg2rad(lat2 - lat1)
  const dLon = deg2rad(lng2 - lng1)
  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2)
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
  const d = R * c * 1000
  return d
}

export const isSamePosition = (
  a: google.maps.LatLngLiteral | null,
  b: google.maps.LatLngLiteral | null
) => a?.lat === b?.lat && a?.lng === b?.lng
