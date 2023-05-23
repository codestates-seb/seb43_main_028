import ContentLoader from 'react-content-loader'

export default function CalendarLoading() {
  return (
    <ContentLoader
      speed={2}
      // viewBox='0 0 400 900'
      width='100%'
      height='250'
      backgroundColor='#e3e3e3'
      foregroundColor='#ecebeb'
    >
      <rect x='20' y='20' rx='8' ry='8' width='90%' height='228' />
    </ContentLoader>
  )
}
