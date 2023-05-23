import styles from './UnregisterInfo.module.scss'

export default function UnregisterInfo() {
  return (
    <div className={styles.container}>
      <div className={styles.infoTitle}>회원탈퇴 관련 안내</div>
      <ol className={styles.infoListBox}>
        <li className={styles.infoList}>- 같은 이메일로는 재가입이 불가능합니다.</li>
        <li className={styles.infoList}>- 기록하신 모든 내용은 삭제됩니다.</li>
        <li className={styles.infoList}>- 회원정보는 탈퇴 후 90일 간 보관됩니다.</li>
      </ol>
    </div>
  )
}
