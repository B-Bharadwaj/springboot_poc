const TOKEN_KEY = 'token';
const FLASH_KEY = 'flash';

export function getToken() {
  return sessionStorage.getItem(TOKEN_KEY);
}

export function setToken(token) {
  sessionStorage.setItem(TOKEN_KEY, token);
}

export function clearToken() {
  sessionStorage.removeItem(TOKEN_KEY);
}

// Reads the JWT payload (base64url); the signature is checked only by the backend.
export function decodeToken(token) {
  try {
    const base64 = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
    const padded = base64 + '='.repeat((4 - (base64.length % 4)) % 4);
    const bytes = Uint8Array.from(atob(padded), (c) => c.charCodeAt(0));
    return JSON.parse(new TextDecoder().decode(bytes));
  } catch {
    return null;
  }
}

// Returns {email, role, exp} for a present, unexpired token, otherwise null.
export function getSession() {
  const token = getToken();
  if (!token) return null;
  const payload = decodeToken(token);
  if (!payload || !payload.exp || payload.exp * 1000 <= Date.now()) return null;
  return { email: payload.sub, role: payload.role, exp: payload.exp };
}

// One-time message shown on the login page after a redirect.
export function setFlash(message) {
  sessionStorage.setItem(FLASH_KEY, message);
}

export function peekFlash() {
  return sessionStorage.getItem(FLASH_KEY);
}

export function clearFlash() {
  sessionStorage.removeItem(FLASH_KEY);
}
