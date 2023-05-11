export const googleMapOptions = {
  styles: [
    {
      // 색상
      featureType: 'all',
      stylers: [
        {
          saturation: -100,
        },
      ],
    },
    {
      // 물 색상
      featureType: 'water',
      stylers: [
        {
          color: '#7dcdcd',
        },
      ],
    },
    {
      // 건물 이름 가리기
      featureType: 'all',
      elementType: 'labels',
      stylers: [{ visibility: 'off' }],
    },

    {
      // 지도 단순화하기
      featureType: 'road',
      elementType: 'geometry',
      stylers: [
        { visibility: 'simplified' },
        { hue: '#000000' },
        { saturation: -50 },
        { lightness: -15 },
        { weight: 1.5 },
      ],
    },
  ],
  mapTypeControl: false, // 지도 위성 끄기
  streetViewControl: false, // 사람 모양 끄기
  gestureHandling: 'greedy', // 한 손 가락으로 지도 핸들링
}

export const polylineOptions = {
  strokeColor: '#55de50',
  strokeOpacity: 1,
  strokeWeight: 6,
}
