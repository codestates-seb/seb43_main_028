import ContentLoader from 'react-content-loader'

export default function FeedLoading() {
  return (
    <ContentLoader
      speed={2}
      width='100%'
      height='100%'
      backgroundColor='#e3e3e3'
      foregroundColor='#ecebeb'
    >
      <rect x='20' y='20' rx='8' ry='8' width='90%' height='228' />
      <rect x='20' y='268' rx='8' ry='8' width='90%' height='228' />
      <rect x='20' y='516' rx='8' ry='8' width='90%' height='228' />
    </ContentLoader>
  )
}
