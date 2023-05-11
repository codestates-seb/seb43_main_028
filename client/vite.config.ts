import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': {
        target: 'https://allforyoumain28.shop',
        changeOrigin: true,
        rewrite: path => path.replace(/^\/api/, ''),
        secure: false,
        ws: true,
      },
    },
  },
})
