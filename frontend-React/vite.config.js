import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react(),  tailwindcss(),],
  server: {
    port: 5173,
    strictPort: true, // This will make Vite fail if port 5173 is not available
  }
})
