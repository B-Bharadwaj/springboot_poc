import { useEffect, useState } from 'react';
import { Navigate, useNavigate } from 'react-router-dom';
import { api, bannerMessage, fieldErrorMap, hasFieldErrors } from '../api.js';
import { clearFlash, getSession, peekFlash, setToken } from '../auth.js';
import EndpointList from '../components/EndpointList.jsx';
import ErrorBanner from '../components/ErrorBanner.jsx';
import FieldError from '../components/FieldError.jsx';

export default function LoginPage() {
  const navigate = useNavigate();
  const [tab, setTab] = useState('login');
  const [banner, setBanner] = useState(() => {
    const flash = peekFlash();
    return flash ? { type: 'error', text: flash } : null;
  });
  const [login, setLogin] = useState({ email: '', password: '' });
  const [register, setRegister] = useState({ name: '', email: '', password: '' });
  const [loginErrors, setLoginErrors] = useState({});
  const [loginMessage, setLoginMessage] = useState('');
  const [registerErrors, setRegisterErrors] = useState({});
  const [busy, setBusy] = useState(false);

  // Removed after the first render so a reload does not show it again.
  useEffect(() => clearFlash(), []);

  if (getSession()) return <Navigate to="/products" replace />;

  function switchTab(next) {
    setTab(next);
    setBanner(null);
  }

  async function submitLogin(e) {
    e.preventDefault();
    setBusy(true);
    setLoginErrors({});
    setLoginMessage('');
    try {
      const data = await api('/api/auth/login', { method: 'POST', body: login });
      setToken(data.token);
      navigate('/products', { replace: true });
    } catch (err) {
      if (err.status === 401) setLoginMessage(err.message);
      else if (hasFieldErrors(err)) setLoginErrors(fieldErrorMap(err));
      else setBanner({ type: 'error', text: bannerMessage(err) });
      setBusy(false);
    }
  }

  async function submitRegister(e) {
    e.preventDefault();
    setBusy(true);
    setRegisterErrors({});
    try {
      const user = await api('/api/auth/register', { method: 'POST', body: register });
      setRegister({ name: '', email: '', password: '' });
      setLogin({ email: user.email, password: '' });
      setTab('login');
      setBanner({ type: 'success', text: `Registered ${user.email}. Please log in.` });
    } catch (err) {
      if (hasFieldErrors(err)) setRegisterErrors(fieldErrorMap(err));
      else setBanner({ type: 'error', text: bannerMessage(err) });
    } finally {
      setBusy(false);
    }
  }

  return (
    <main className="container narrow">
      <h1>Order POC</h1>
      <EndpointList endpoints={[['POST', '/api/auth/login'], ['POST', '/api/auth/register']]} />
      <div className="tabs" role="tablist">
        <button type="button" role="tab" aria-selected={tab === 'login'}
          className={tab === 'login' ? 'tab active' : 'tab'} onClick={() => switchTab('login')}>
          Login
        </button>
        <button type="button" role="tab" aria-selected={tab === 'register'}
          className={tab === 'register' ? 'tab active' : 'tab'} onClick={() => switchTab('register')}>
          Register
        </button>
      </div>

      <ErrorBanner banner={banner} onClose={() => setBanner(null)} />

      {tab === 'login' ? (
        <form className="card" onSubmit={submitLogin} noValidate>
          <label htmlFor="login-email">Email</label>
          <input id="login-email" type="email" autoComplete="username" value={login.email}
            onChange={(e) => setLogin({ ...login, email: e.target.value })} />
          <FieldError message={loginErrors.email} />

          <label htmlFor="login-password">Password</label>
          <input id="login-password" type="password" autoComplete="current-password" value={login.password}
            onChange={(e) => setLogin({ ...login, password: e.target.value })} />
          <FieldError message={loginErrors.password} />

          <button type="submit" disabled={busy}>
            {busy ? 'Logging in…' : 'Log in'}
            <span className="btn-endpoint">POST /api/auth/login</span>
          </button>
          <FieldError message={loginMessage} />
        </form>
      ) : (
        <form className="card" onSubmit={submitRegister} noValidate>
          <label htmlFor="reg-name">Name</label>
          <input id="reg-name" value={register.name}
            onChange={(e) => setRegister({ ...register, name: e.target.value })} />
          <FieldError message={registerErrors.name} />

          <label htmlFor="reg-email">Email</label>
          <input id="reg-email" type="email" autoComplete="username" value={register.email}
            onChange={(e) => setRegister({ ...register, email: e.target.value })} />
          <FieldError message={registerErrors.email} />

          <label htmlFor="reg-password">Password <span className="hint">(8 to 72 characters)</span></label>
          <input id="reg-password" type="password" autoComplete="new-password" value={register.password}
            onChange={(e) => setRegister({ ...register, password: e.target.value })} />
          <FieldError message={registerErrors.password} />

          <button type="submit" disabled={busy}>
            {busy ? 'Registering…' : 'Register'}
            <span className="btn-endpoint">POST /api/auth/register</span>
          </button>
        </form>
      )}
    </main>
  );
}
