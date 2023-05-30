import ContentLoader from 'react-content-loader'
import styles from './HistoryDetailLoading.module.scss'
import Header from '../../components/common/Header'

function HeaderBox() {
  return (
    <div>
      <Header
        hasBackButton
        hasCloseButton={false}
        headerTitle='상세 기록'
        handleCloseFn={() => {}}
        path='-1'
      />
      <div className={styles.header}>
        <ContentLoader
          speed={2}
          width='100%'
          height='100%'
          backgroundColor='#e3e3e3'
          foregroundColor='#ecebeb'
        >
          <rect x='20' y='20' rx='8' ry='8' width='110' height='32' />
          <rect x='20' y='90' rx='3' ry='3' width='245' height='16' />
          <rect x='20' y='120' rx='3' ry='3' width='120' height='16' />
        </ContentLoader>
      </div>
    </div>
  )
}

export default function HistoryDetailLoading() {
  return (
    <>
      <HeaderBox />
      <ContentLoader
        speed={2}
        width='100%'
        height='100%'
        backgroundColor='#e3e3e3'
        foregroundColor='#ecebeb'
      >
        <rect x='20' y='20' rx='8' ry='8' width='90%' height='228' />
        <rect x='20' y='268' rx='8' ry='8' width='90%' height='96' />
        <rect x='20' y='384' rx='8' ry='8' width='90%' height='336' />
      </ContentLoader>
    </>
  )
}
