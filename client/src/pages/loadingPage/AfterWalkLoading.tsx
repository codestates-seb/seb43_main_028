import ContentLoader from 'react-content-loader'
import styles from './AfterWalkLoading.module.scss'

function Header() {
  return (
    <div className={styles.header}>
      <ContentLoader
        speed={2}
        width='100%'
        height='55'
        backgroundColor='#e3e3e3'
        foregroundColor='#ecebeb'
      >
        <circle cx='24' cy='24' r='24' />
        <rect x='64' y='0' rx='3' ry='3' width='118' height='16' />
        <rect x='64' y='20' rx='3' ry='3' width='180' height='24' />
      </ContentLoader>
      <div className={styles.openSettingBox}>
        <p className={styles.openTitle}>기록공개 여부</p>
        <p className={styles.openOption}>나만 보기</p>
      </div>
      <input className={styles.input} type='text' placeholder='한 줄 메시지 작성' disabled />
      <div className={styles.btnBox}>
        <div className={styles.noSaveBtn}>
          <div className={styles.noSave}>저장안함</div>
        </div>
        <div className={styles.completeBtn}>
          <div className={styles.complete}>완료</div>
        </div>
      </div>
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
    <>
      <Header />
      <ContentLoader
        speed={2}
        width='100%'
        height='240'
        backgroundColor='#e3e3e3'
        foregroundColor='#ecebeb'
      >
        <rect x='0' y='0' width='100%' height='240' />
      </ContentLoader>
      <div className={styles.snapFormBox}>
        <div>순간기록</div>
        <div className={styles.snapFrom}>
          <SnapForm />
          <SnapForm />
          <SnapForm />
        </div>
      </div>
    </>
  )
}
