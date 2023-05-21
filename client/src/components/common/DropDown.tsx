import { useEffect, useState } from 'react'
import styles from './DropDown.module.scss'
import Icon from './Icon'

type DropDownProps = {
  currentSetting: keyof typeof engOptionsObj
  onSubmit: (option: string) => void
}

const options = ['전체 공개', '나만 보기']
const engOptionsObj = { PUBLIC: options[0], PRIVATE: options[1] }
function DropDown({ currentSetting, onSubmit }: DropDownProps) {
  const [isOptionOpened, setIsOptionOpened] = useState(false)
  const [selectedOptionTitle, setSelectedOptionTitle] = useState(engOptionsObj[currentSetting])

  const handleChangeCurrentOption = (option: string) => {
    setSelectedOptionTitle(option)
    setIsOptionOpened(false)

    const engOption = Object.keys(engOptionsObj).find(engOpt => engOptionsObj[engOpt] === option)
    if (engOption) onSubmit(engOption)
  }

  const handleOpenOptions = () => {
    setIsOptionOpened(prev => !prev)
  }

  useEffect(() => {
    setSelectedOptionTitle(engOptionsObj[currentSetting])
  }, [currentSetting])

  return (
    <ul className={styles.listContainer}>
      <button
        className={isOptionOpened ? styles.open : styles.listTitle}
        type='button'
        onClick={handleOpenOptions}
      >
        <p>{selectedOptionTitle}</p>
        <Icon name='arrow-drop-down' size={16} />
      </button>
      {isOptionOpened && (
        <div className={styles.listBox}>
          {options.map(option => {
            return (
              <li key={crypto.randomUUID()}>
                <button type='button' onClick={() => handleChangeCurrentOption(option)}>
                  {option}
                </button>
              </li>
            )
          })}
        </div>
      )}
    </ul>
  )
}

export default DropDown
