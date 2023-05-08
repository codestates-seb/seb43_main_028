import styles from './SnapForm.module.scss'
import ImgInput from './ImgInput'

interface HeaderProps {
  onCancel: React.MouseEventHandler<HTMLButtonElement>
}

function Header({ onCancel }: HeaderProps) {
  return (
    <div className={styles.header}>
      <h1>순간기록 남기기</h1>
      <div className={styles.btnBox}>
        <button type='button' onClick={onCancel}>
          취소
        </button>
        <button type='submit'>완료</button>
      </div>
    </div>
  )
}
export default function SnapForm() {
  const handleSubmit = () => {
    console.log('순간기록 전송')
  }

  const handleCancel = () => {
    console.log('순간 기록 취소')
  }

  return (
    <div className={styles.container}>
      <form onSubmit={handleSubmit}>
        <div>
          <Header onCancel={handleCancel} />
          <ImgInput />
        </div>
        <label htmlFor='text'>
          <textarea className={styles.textInput} id='text' name='text' rows={10} />
        </label>
      </form>
    </div>
  )
}
