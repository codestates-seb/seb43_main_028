import { useEffect, useRef } from 'react'

export default function useInterval(callback: () => void, delay = 1000) {
  const savedCallback = useRef<() => void>()

  useEffect(() => {
    savedCallback.current = callback
  }, [callback])

  useEffect(() => {
    function tick() {
      savedCallback.current?.()
    }
    const id = setInterval(tick, delay)
    return () => clearInterval(id)
  }, [delay])
}
