import { clearToken, getToken, setFlash } from './auth.js';

const SESSION_EXPIRED = 'Session expired, please log in again';
const MAX_ACTIVITY = 50;

// API activity log: method, path, status, time only (no tokens, bodies or headers).
let activity = [];
let nextActivityId = 1;
const listeners = new Set();

function record(method, path, status, ms, error) {
  const entry = { id: nextActivityId++, time: new Date(), method, path, status, ms, error };
  activity = [entry, ...activity].slice(0, MAX_ACTIVITY);
  listeners.forEach((listener) => listener());
}

export function subscribeActivity(listener) {
  listeners.add(listener);
  return () => listeners.delete(listener);
}

export function getActivity() {
  return activity;
}

export function clearActivity() {
  activity = [];
  listeners.forEach((listener) => listener());
}

// Sends a JSON request to the backend; throws {status, message, fieldErrors} on failure.
export async function api(path, { method = 'GET', body, auth = true } = {}) {
  const headers = {};
  const token = getToken();
  if (auth && token) headers.Authorization = `Bearer ${token}`;
  if (body !== undefined) headers['Content-Type'] = 'application/json';

  const start = performance.now();
  let response;
  try {
    response = await fetch(path, {
      method,
      headers,
      body: body !== undefined ? JSON.stringify(body) : undefined,
    });
  } catch {
    record(method, path, 0, performance.now() - start, 'Cannot reach the server');
    throw { status: 0, message: 'Cannot reach the server', fieldErrors: [] };
  }

  // Reading text first handles 204 and other empty bodies.
  const text = await response.text();
  const ms = performance.now() - start;
  let data = null;
  if (text) {
    try {
      data = JSON.parse(text);
    } catch {
      data = null;
    }
  }

  // The dev proxy sends an empty 502 when the backend is not running.
  if (response.status === 502 && !data) {
    record(method, path, 0, ms, 'Cannot reach the server');
    throw { status: 0, message: 'Cannot reach the server', fieldErrors: [] };
  }

  if (!response.ok) {
    const err = {
      status: response.status,
      message: (data && data.message) || `Request failed (status ${response.status})`,
      fieldErrors: (data && data.fieldErrors) || [],
    };
    record(method, path, err.status, ms, bannerMessage(err));
    const isAuthCall = path.startsWith('/api/auth/');
    if (response.status === 401 && auth && !isAuthCall) {
      clearToken();
      setFlash(SESSION_EXPIRED);
      window.location.assign('/login');
    }
    throw err;
  }
  record(method, path, response.status, ms, null);
  return data;
}

// Text for the red banner; hides server details on 5xx.
export function bannerMessage(err) {
  if (err.status === 0) return 'Cannot reach the server';
  if (err.status === 403) return "You don't have permission to do that";
  if (err.status >= 500) return 'Something went wrong, please try again';
  return err.message;
}

// Turns [{field, message}] into {field: message}.
export function fieldErrorMap(err) {
  const map = {};
  for (const fe of err.fieldErrors || []) {
    map[fe.field] = map[fe.field] ? `${map[fe.field]}; ${fe.message}` : fe.message;
  }
  return map;
}

export function hasFieldErrors(err) {
  return err.status === 400 && err.fieldErrors && err.fieldErrors.length > 0;
}
