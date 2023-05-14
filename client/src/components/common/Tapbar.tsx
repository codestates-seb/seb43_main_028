import { NavLink } from 'react-router-dom'
import { TapBarElementITF } from '../../types/TapBarElement'
import Icon from './Icon'
import styles from './Tapbar.module.scss'

type TapbarProps = {
  tapBarContent: TapBarElementITF[]
}

export default function Tapbar({ tapBarContent }: TapbarProps) {
  return (
    <div className={styles.container}>
      <ul className={styles.tapList}>
        {tapBarContent.map(element => {
          return (
            <li key={element.id} className={styles.tapItem}>
              <NavLink className={styles.tapLink} to={element.path}>
                {({ isActive }) => (
                  <div className={isActive ? `${styles.active}` : ''}>
                    <Icon name='map-color' />
                    {element.label}
                  </div>
                )}
              </NavLink>
            </li>
          )
        })}
      </ul>
    </div>
  )
}
