import { NavLink } from 'react-router-dom'
import { TapBarElementType } from '../../router/routerData'
import Icon from './Icon'
import styles from './Tapbar.module.scss'

type TapbarProps = {
  tapBarContent: TapBarElementType[]
}

enum TapBarIconName {
  홈 = 'tapbar-home',
  기록 = 'tapbar-log',
  피드 = 'tapbar-feed',
  내정보 = 'tapbar-my',
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
                  <div className={isActive ? `${styles.active}` : `${styles.inactive}`}>
                    <Icon
                      name={`${TapBarIconName[element.label as keyof typeof TapBarIconName]}${
                        isActive ? '-active' : ''
                      }`}
                    />
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
