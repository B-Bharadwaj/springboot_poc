import { Link } from 'react-router-dom';

export default function NotFoundPage() {
  return (
    <main className="container narrow">
      <h1>Page not found</h1>
      <p>There is nothing at this address.</p>
      <Link to="/products">Go to products</Link>
    </main>
  );
}
