import { Navigate, Outlet } from 'react-router-dom';
import { clearToken, getSession, getToken, setFlash } from '../auth.js';
import Navbar from './Navbar.jsx';

// Shows the page with the navbar, or sends the user to /login.
export default function ProtectedRoute() {
  const session = getSession();
  if (!session) {
    if (getToken()) {
      clearToken();
      setFlash('Session expired, please log in again');
    }
    return <Navigate to="/login" replace />;
  }
  return (
    <>
      <Navbar session={session} />
      <main className="container">
        <Outlet />
      </main>
    </>
  );
}
