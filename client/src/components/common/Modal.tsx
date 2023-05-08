import React from 'react'
import styles from './modal.module.scss'

type Option = {
  label: string
  handleClick: () => void
  handleKeyDown: (e: React.KeyboardEvent<Element>) => void
  id: number
}

type Props = {
  dummyData: {
    title: string
    options: Option[]
  }
  onClose: () => void
}

function Modal({ dummyData, onClose }: Props): JSX.Element {
  const handleBackgroundClick = (
    event: React.MouseEvent<HTMLDivElement> | React.MouseEvent<HTMLButtonElement>
  ) => {
    // event.target -> 실제로 클릭된 요소
    // event.currentTarget -> 이벤트가 바인딩된 요소
    // 백그라운드를 클릭했을 때, 그 클릭이 모달 자체를 클릭한 것인지 확인
    if (event.target === event.currentTarget) {
      onClose()
    }
  }
  const handleKeyDown = (e: React.KeyboardEvent) => {
    console.log(e)
  }

  return (
    <>
      <div
        role='presentation'
        className={styles.modalBackground}
        onClick={handleBackgroundClick}
        onKeyDown={e => handleKeyDown(e)}
      />
      <div className={styles.modal}>
        <div className={styles.modalTop}>
          <div>{dummyData.title}</div>
          <button className={styles.xBtn} type='button' onClick={handleBackgroundClick}>
            𝖷
          </button>
        </div>
        <ul className={styles.optionsContainer}>
          {dummyData.options.map(option => {
            return (
              <li
                role='presentation'
                key={option.id}
                onClick={option.handleClick}
                onKeyDown={e => option.handleKeyDown(e)}
              >
                {option.label}
              </li>
            )
          })}
        </ul>
      </div>
    </>
  )
}

export default Modal
