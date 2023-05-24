import Icon, { IconNameType } from '../Icon'
import styles from './MapButton.module.scss'

type MapButtonProps = {
  name: IconNameType
  handleClick: () => void
}

export function MapButton({ name, handleClick }: MapButtonProps) {
  return (
    <button className={styles.button} type='button' onClick={handleClick}>
      <Icon name={name} />
    </button>
  )
}
