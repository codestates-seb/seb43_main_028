import ContentLoader from 'react-content-loader'
import styles from './HistoryListLoading.module.scss'

export default function HistoryListLoading() {
  return (
    <>
      <div className={styles.header}>
        <ContentLoader
          speed={2}
          // viewBox='0 0 400 900'
          width='100%'
          height='100%'
          backgroundColor='#e3e3e3'
          foregroundColor='#ecebeb'
        >
          <circle cx='50' cy='50' r='24' />
          <rect x='90' y='20' rx='3' ry='3' width='160' height='16' />
          <rect x='90' y='52' rx='3' ry='3' width='140' height='16' />
          <rect x='90' y='88' rx='3' ry='3' width='200' height='16' />
        </ContentLoader>
      </div>
      <ContentLoader
        speed={2}
        // viewBox='0 0 400 900'
        width='100%'
        height='100%'
        backgroundColor='#e3e3e3'
        foregroundColor='#ecebeb'
      >
        <rect x='20' y='20' rx='8' ry='8' width='90%' height='32' />
        <rect x='20' y='72' rx='8' ry='8' width='90%' height='228' />
        <rect x='20' y='320' rx='8' ry='8' width='90%' height='228' />
        <rect x='20' y='568' rx='8' ry='8' width='90%' height='228' />
      </ContentLoader>
    </>
  )
}
