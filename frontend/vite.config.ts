import tailwindcss from 'tailwindcss';
import react from '@vitejs/plugin-react'
import {defineConfig, loadEnv} from 'vite';

// https://vite.dev/config/
export default defineConfig(({mode}) => {
  const env = loadEnv(mode, process.cwd());

  return {
    plugins: [react()],
    css: {
      postcss: {
        plugins: [tailwindcss()]
      }
    },
    resolve: {
      alias: {
        '@': '/src'
      }
    },
    server: {
      port: parseInt(env.VITE_PORT) || 5173
    }
  };
});