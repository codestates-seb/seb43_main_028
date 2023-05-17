type MapOptionsType = google.maps.MapOptions

export const mapOptions: MapOptionsType = {
  zoom: 18,
  center: { lat: 37.7749, lng: -122.4194 },
  mapTypeControl: false,
  streetViewControl: false,
  gestureHandling: 'greedy',
  zoomControl: false,
  fullscreenControl: true,
  scaleControl: false,
  styles: [
    { featureType: 'all', elementType: 'labels', stylers: [{ visibility: 'off' }] },
    { featureType: 'road', elementType: 'geometry', stylers: [] },
  ],
}

export const polylineOptions = {
  strokeColor: '#55de50',
  strokeOpacity: 1,
  strokeWeight: 6,
}
