import ContentLoader from 'react-content-loader'

export default function HomeLoading() {
  return (
    <ContentLoader
      speed={2}
      // viewBox='0 0 400 900'
      width='100%'
      height='100%'
      backgroundColor='#e3e3e3'
      foregroundColor='#ecebeb'
    >
      <circle cx='60' cy='60' r='40' width='150' height='150' />
      <rect x='120' y='30' rx='3' ry='3' width='100' height='24' />
      <rect x='120' y='75' rx='3' ry='3' width='200' height='16' />
      <rect x='0' y='130' width='100%' height='100%' />
    </ContentLoader>
  )
}
