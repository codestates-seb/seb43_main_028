import ContentLoader from 'react-content-loader'
import styles from './AfterWalkLoading.module.scss'

function Header() {
  return (
    <div className={styles.header}>
      <ContentLoader
        speed={2}
        width='100%'
        height='230'
        backgroundColor='#e3e3e3'
        foregroundColor='#ecebeb'
      >
        <circle cx='24' cy='24' r='24' />
        <rect x='64' y='0' rx='3' ry='3' width='118' height='16' />
        <rect x='64' y='20' rx='3' ry='3' width='180' height='24' />
        <rect x='0' y='64' rx='3' ry='3' width='100' height='24' />
        <rect x='85%' y='64' rx='3' ry='3' width='44.5' height='18' />
        <rect x='0' y='102' rx='3' ry='3' width='100%' height='55' />
        <rect x='0' y='177' rx='3' ry='3' width='48%' height='48' />
        <rect x='52%' y='177' rx='3' ry='3' width='48%' height='48' />
      </ContentLoader>
    </div>
  )
}

function SnapForm() {
  return (
    <ContentLoader
      speed={2}
      width='100%'
      height='80'
      backgroundColor='#e3e3e3'
      foregroundColor='#ecebeb'
    >
      <rect x='0' y='0' rx='12' ry='12' width='120' height='80' />
      <rect x='128' y='0' rx='3' ry='3' width='48' height='12' />
      <rect x='128' y='16' rx='3' ry='3' width='150' height='12' />
      <rect x='128' y='32' rx='3' ry='3' width='150' height='12' />
      <rect x='128' y='48' rx='3' ry='3' width='150' height='12' />
      <rect x='128' y='64' rx='3' ry='3' width='47' height='12' />
      <rect x='179' y='64' rx='3' ry='3' width='47' height='12' />
    </ContentLoader>
  )
}

export default function AfterWalkLoading() {
  return (
    <div className={styles.container}>
      <Header />
      <ContentLoader
        speed={2}
        width='100%'
        height='290'
        backgroundColor='#e3e3e3'
        foregroundColor='#ecebeb'
      >
        <rect x='0' y='0' width='100%' height='240' />
        <rect x='20' y='260' rx='3' ry='3' width='100' height='24' />
      </ContentLoader>
      <div className={styles.snapFrom}>
        <SnapForm />
        <SnapForm />
        <SnapForm />
      </div>
    </div>
  )
}
