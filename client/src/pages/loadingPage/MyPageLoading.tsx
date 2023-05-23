import ContentLoader from 'react-content-loader'
import styles from './MyPageLoading.module.scss'

function ProfileBox() {
  return (
    <div className={styles.profile}>
      <ContentLoader
        speed={2}
        width='100%'
        height='75%'
        backgroundColor='#e3e3e3'
        foregroundColor='#ecebeb'
      >
        <rect x='0' y='0' rx='8' ry='8' width='72' height='42' />
        <circle cx='80%' cy='23%' r='32' width='150' height='150' />
        <rect x='0' y='60' rx='3' ry='3' width='100' height='12' />
        <rect x='0' y='80' rx='3' ry='3' width='120' height='12' />
      </ContentLoader>
      <p className={styles.editProfileBtn}>프로필 수정하기</p>
    </div>
  )
}

export default function MypPageLoading() {
  return (
    <div className={styles.container}>
      <ProfileBox />
      <div className={styles.configBox}>
        <p className={styles.configTitle}>걷기 기록 공개 설정</p>
        <ContentLoader
          speed={2}
          width='100%'
          height='72'
          backgroundColor='#e3e3e3'
          foregroundColor='#ecebeb'
        >
          <rect x='0' y='20' rx='8' ry='8' width='110' height='32' />
        </ContentLoader>
        <p className={styles.configMessage}>
          {' '}
          전체공개 시 내 걷기 기록이 피드 탭에서 모든 유저에게 보입니다.
        </p>
      </div>
      <p className={styles.setLinkWrapper900}>비밀번호 변경하기</p>
      <p className={styles.setLinkWrapper900}>로그아웃</p>
      <p className={styles.setLinkWrapper600}>회원 탈퇴하기</p>
    </div>
  )
}
