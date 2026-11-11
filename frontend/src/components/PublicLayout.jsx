import { Outlet } from 'react-router-dom';
import { getSession } from '../auth.js';
import Navbar from './Navbar.jsx';

// Navbar plus page for routes that work without logging in.
export default function PublicLayout() {
  return (
    <>
      <Navbar session={getSession()} />
      <main className="container">
        <Outlet />
      </main>
    </>
  );
}
