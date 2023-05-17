import { useState } from 'react'
import styles from './DropDown.module.scss'
import Icon from './Icon'

type DropDownProps = {
  options: {
    id: number
    title: string
    handleClick: Promise<void>
  }[]
}

function DropDown({ options }: DropDownProps) {
  const [isOptionOpened, setIsOptionOpened] = useState(false)
  const [selectedOptionId, setSelectedOptionId] = useState(0)
  const [selectedOptionTitle, setSelectedOptionTitle] = useState(options[0].title)

  const handleChangeCurrentOption = (title: string, changeSetting: () => void) => {
    setSelectedOptionTitle(title)
    setIsOptionOpened(prev => !prev)
    changeSetting()
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
            <Icon name='arrow-drop-down' />
          </button>
        </div>
      </div>
      {isOptionOpened ? (
        <div className={styles.listBox}>
          {options.map(option => {
            return (
              <li key={option.id}>
                <button
                  type='button'
                  onClick={() => handleChangeCurrentOption(option.title, option.handleClick)}
                >
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
