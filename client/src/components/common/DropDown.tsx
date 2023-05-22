import { useEffect, useState } from 'react'
import styles from './DropDown.module.scss'
import Icon from './Icon'

type DropDownProps = {
  currentSetting: 'PUBLIC' | 'PRIVATE'
  onSubmit: (option: string) => void
}

type OptionType = '전체 공개' | '나만 보기'

const options: OptionType[] = ['전체 공개', '나만 보기']
const engOptionsObj: { [key: string]: OptionType } = {
  PUBLIC: '전체 공개',
  PRIVATE: '나만 보기',
}
function DropDown({ currentSetting, onSubmit }: DropDownProps) {
  const [isOptionOpened, setIsOptionOpened] = useState<boolean>(false)
  const [selectedOptionTitle, setSelectedOptionTitle] = useState<OptionType>(
    engOptionsObj[currentSetting]
  )

  const handleChangeCurrentOption = (option: OptionType) => {
    setSelectedOptionTitle(option)
    setIsOptionOpened(false)

    const engOption = Object.keys(engOptionsObj).find(key => engOptionsObj[key] === option)

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
