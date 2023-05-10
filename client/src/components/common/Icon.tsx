import SpriteSVG from '../../assets/sprite.svg'

type IconName =
  | 'camera-color'
  | 'camera-oval'
  | 'close'
  | 'edit-gray'
  | 'map-color'
  | 'no-profile'
  | 'pin'
  | 'refresh'
  | 'time-gray'
  | 'trash-gray'
  | 'no-image'
  | 'arrow-right'

interface IconProps {
  name: IconName
  size?: 16 | 24 | 48
}

export default function Icon({ name, size = 24 }: IconProps) {
  return (
    <svg width={size} height={size}>
      <use href={`${SpriteSVG}#${name}`} />
    </svg>
  )
}
