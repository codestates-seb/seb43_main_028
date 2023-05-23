import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': {
        target: 'https://32bc-49-171-175-238.ngrok-free.app',
        changeOrigin: true,
        secure: false,
        rewrite: path => path.replace(/^\/api/, ''),
      },
    },
  },
})
