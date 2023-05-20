import { useState } from 'react'
import styles from './DropDown.module.scss'
import Icon from './Icon'

type DropDownProps = {
  options: {
    id: number
    title: string
    handleClick: (title: string) => void
    param: string
  }[]
}

type OptionType = {
  id: number
  title: string
  handleClick: (title: string) => void
  param: string
}

function DropDown({ options }: DropDownProps) {
  const [isOptionOpened, setIsOptionOpened] = useState(false)
  const [selectedOptionTitle, setSelectedOptionTitle] = useState(options[0].title)

  const handleChangeCurrentOption = (option: OptionType) => {
    setSelectedOptionTitle(option.title)
    setIsOptionOpened(prev => !prev)
    option.handleClick(option.param)
  }
  const handleOpenOptions = () => {
    setIsOptionOpened(prev => !prev)
  }
  return (
    <ul className={styles.listContainer}>
      <div className={`${styles.listTitle} ${isOptionOpened ? styles.open : ''}`}>
        <button type='button' onClick={handleOpenOptions}>
          {selectedOptionTitle}
        </button>
        <div className={styles.buttonWrapper}>
          <button className={styles.openBtn} type='button' onClick={handleOpenOptions}>
            <Icon name='arrow-drop-down' size={16} />
          </button>
        </div>
      </div>
      {isOptionOpened ? (
        <div className={styles.listBox}>
          {options.map(option => {
            return (
              <li key={option.id}>
                <button type='button' onClick={() => handleChangeCurrentOption(option)}>
                  {option.title}
                </button>
              </li>
            )
          })}
        </div>
      ) : null}
    </ul>
  )
}

export default DropDown
