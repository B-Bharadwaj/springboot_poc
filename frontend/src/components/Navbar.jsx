import { Link, NavLink, useNavigate } from 'react-router-dom';
import { clearToken } from '../auth.js';

export default function Navbar({ session }) {
  const navigate = useNavigate();

  function logout() {
    clearToken();
    navigate('/login', { replace: true });
  }

  return (
    <header className="navbar">
      <nav className="nav-links">
        <Link to="/overview" className="brand">Order POC</Link>
        <NavLink to="/products">Products</NavLink>
        <NavLink to="/order">Place order</NavLink>
        <NavLink to="/orders">Orders</NavLink>
        <NavLink to="/demo">Demo tools</NavLink>
      </nav>
      {/* No session on public pages when logged out. */}
      {session && (
        <div className="nav-user">
          <span>{session.email}</span>
          <span className={`badge badge-${(session.role || '').toLowerCase()}`}>{session.role}</span>
          <button type="button" className="btn-secondary" onClick={logout}>
            Logout
          </button>
        </div>
      )}
    </header>
  );
}
