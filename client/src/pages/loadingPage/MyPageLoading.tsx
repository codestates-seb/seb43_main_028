import ContentLoader from 'react-content-loader'
import styles from './MyPageLoading.module.scss'

function ProfileBox() {
  return (
    <div className={styles.profile}>
      <ContentLoader
        speed={2}
        width='100%'
        height='162'
        backgroundColor='#e3e3e3'
        foregroundColor='#ecebeb'
      >
        <rect x='0' y='0' rx='8' ry='8' width='72' height='42' />
        <circle cx='80%' cy='23%' r='32' width='150' height='150' />
        <rect x='0' y='60' rx='3' ry='3' width='100' height='12' />
        <rect x='0' y='80' rx='3' ry='3' width='120' height='12' />
        <rect x='0' y='132' rx='3' ry='3' width='100%' height='30' />
      </ContentLoader>
    </div>
  )
}

export default function MyPageLoading() {
  return (
    <div className={styles.container}>
      <ProfileBox />
      <div className={styles.configBox}>
        <ContentLoader
          speed={2}
          width='100%'
          height='135'
          backgroundColor='#e3e3e3'
          foregroundColor='#ecebeb'
        >
          <rect x='0' y='0' rx='3' ry='3' width='130' height='24' />
          <rect x='0' y='44' rx='3' ry='3' width='110' height='32' />
          <rect x='0' y='92' rx='3' ry='3' width='100%' height='16' />
          <rect x='0' y='115' rx='3' ry='3' width='80' height='16' />
        </ContentLoader>
      </div>
      <div className={styles.bottom}>
        <ContentLoader
          speed={2}
          width='170'
          height='200'
          backgroundColor='#e3e3e3'
          foregroundColor='#ecebeb'
        >
          <rect x='0' y='40' rx='3' ry='3' width='170' height='24' />
          <rect x='35' y='104' rx='3' ry='3' width='100' height='24' />
          <rect x='15' y='168' rx='3' ry='3' width='140' height='24' />
        </ContentLoader>
      </div>
    </div>
  )
}
