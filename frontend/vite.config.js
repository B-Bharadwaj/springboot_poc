import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// Dev server on 5173; /api is forwarded to Spring Boot on 8081.
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    strictPort: true,
    proxy: {
      '/api': {
        target: 'http://localhost:8081',
        changeOrigin: true,
        // Backend down: answer 502 with no body so the app shows "Cannot reach the server".
        configure: (proxy) => {
          proxy.on('error', (_err, _req, res) => {
            if (!res.headersSent) res.writeHead(502);
            res.end();
          });
        },
      },
    },
  },
});
