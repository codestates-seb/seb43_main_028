import ContentLoader from 'react-content-loader'
import styles from './OnWalkLoading.module.scss'

function Header() {
  return (
    <div className={styles.header}>
      <ContentLoader
        speed={2}
        // viewBox='0 0 400 900'
        width='100%'
        height='100%'
        backgroundColor='#e3e3e3'
        foregroundColor='#ecebeb'
      >
        <circle cx='44' cy='44' r='24' width='150' height='150' />
        <rect x='84' y='20' rx='3' ry='3' width='118' height='16' />
        <rect x='84' y='40' rx='3' ry='3' width='180' height='24' />
        <rect x='20' y='84' rx='8' ry='8' width='90%' height='48' />
      </ContentLoader>
    </div>
  )
}

export default function OnWalkLoading() {
  return (
    <>
      <Header />
      <ContentLoader
        speed={2}
        // viewBox='0 0 400 900'
        width='100%'
        height='100%'
        backgroundColor='#e3e3e3'
        foregroundColor='#ecebeb'
      >
        <rect x='20' y='20' rx='8' ry='8' width='90%' height='217' />
        <rect x='20' y='257' rx='8' ry='8' width='90%' height='476' />
      </ContentLoader>
    </>
  )
}
