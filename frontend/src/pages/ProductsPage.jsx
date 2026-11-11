import { useEffect, useState } from 'react';
import { api, bannerMessage } from '../api.js';
import { getSession } from '../auth.js';
import { formatPrice } from '../format.js';
import EndpointList from '../components/EndpointList.jsx';
import ErrorBanner from '../components/ErrorBanner.jsx';
import ProductForm from '../components/ProductForm.jsx';

const ALL = { type: 'all' };

const READ_ENDPOINTS = [
  ['GET', '/api/products'],
  ['GET', '/api/products/search'],
  ['GET', '/api/products/cheap'],
];

const ADMIN_ENDPOINTS = [
  ['POST', '/api/products'],
  ['PUT', '/api/products/{id}'],
  ['DELETE', '/api/products/{id}'],
];

function urlFor(filter) {
  if (filter.type === 'search') return `/api/products/search?name=${encodeURIComponent(filter.value)}`;
  if (filter.type === 'cheap') return `/api/products/cheap?maxPrice=${encodeURIComponent(filter.value)}`;
  return '/api/products';
}

export default function ProductsPage() {
  const isAdmin = getSession()?.role === 'ADMIN';
  const [products, setProducts] = useState([]);
  const [filter, setFilter] = useState(ALL);
  const [search, setSearch] = useState('');
  const [maxPrice, setMaxPrice] = useState('');
  const [banner, setBanner] = useState(null);
  const [editing, setEditing] = useState(null);
  const [busy, setBusy] = useState(false);

  // Returns true when the list loaded.
  async function load(nextFilter = filter) {
    try {
      setProducts(await api(urlFor(nextFilter)));
      setFilter(nextFilter);
      return true;
    } catch (err) {
      setBanner({ type: 'error', text: bannerMessage(err) });
      return false;
    }
  }

  useEffect(() => {
    load(ALL);
  }, []);

  async function applyFilter(nextFilter) {
    setBusy(true);
    if (await load(nextFilter)) setBanner(null);
    setBusy(false);
  }

  function showError(err) {
    setBanner({ type: 'error', text: bannerMessage(err) });
    if (err.status === 404) load();
  }

  function onSaved(saved) {
    setBanner({ type: 'success', text: `Saved product #${saved.id} (${saved.name})` });
    setEditing(null);
    load();
  }

  async function remove(product) {
    if (!window.confirm(`Delete product #${product.id} (${product.name})?`)) return;
    setBusy(true);
    try {
      await api(`/api/products/${product.id}`, { method: 'DELETE' });
      setBanner({ type: 'success', text: `Deleted product #${product.id}` });
      load();
    } catch (err) {
      showError(err);
    } finally {
      setBusy(false);
    }
  }

  return (
    <>
      <div className="page-header">
        <h1>Products</h1>
        {isAdmin && !editing && (
          <button type="button" onClick={() => setEditing('new')}>Add product</button>
        )}
      </div>
      <EndpointList endpoints={isAdmin ? [...READ_ENDPOINTS, ...ADMIN_ENDPOINTS] : READ_ENDPOINTS} />

      <ErrorBanner banner={banner} onClose={() => setBanner(null)} />

      {editing && (
        <ProductForm
          key={editing === 'new' ? 'new' : editing.id}
          product={editing === 'new' ? null : editing}
          onSaved={onSaved}
          onCancel={() => setEditing(null)}
          onError={showError}
        />
      )}

      <div className="filters">
        <form className="filter" onSubmit={(e) => { e.preventDefault(); applyFilter({ type: 'search', value: search }); }}>
          <label htmlFor="search">Name (exact match)</label>
          <input id="search" value={search} onChange={(e) => setSearch(e.target.value)} />
          <button type="submit" disabled={busy}>
            Search<span className="btn-endpoint">GET /api/products/search</span>
          </button>
          <button type="button" className="btn-secondary" disabled={busy}
            onClick={() => { setSearch(''); applyFilter(ALL); }}>
            Clear<span className="btn-endpoint">GET /api/products</span>
          </button>
        </form>
        <form className="filter" onSubmit={(e) => { e.preventDefault(); applyFilter({ type: 'cheap', value: maxPrice }); }}>
          <label htmlFor="max-price">Cheaper than</label>
          <input id="max-price" type="number" step="0.01" value={maxPrice} onChange={(e) => setMaxPrice(e.target.value)} />
          <button type="submit" disabled={busy}>
            Filter<span className="btn-endpoint">GET /api/products/cheap</span>
          </button>
          <button type="button" className="btn-secondary" disabled={busy}
            onClick={() => { setMaxPrice(''); applyFilter(ALL); }}>
            Clear<span className="btn-endpoint">GET /api/products</span>
          </button>
        </form>
      </div>

      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th className="num">Price</th>
            <th className="num">Quantity</th>
            <th>Description</th>
            {isAdmin && <th>Actions</th>}
          </tr>
        </thead>
        <tbody>
          {products.length === 0 && (
            <tr><td colSpan={isAdmin ? 6 : 5} className="empty">No products</td></tr>
          )}
          {products.map((p) => (
            <tr key={p.id}>
              <td>{p.id}</td>
              <td>{p.name}</td>
              <td className="num">{formatPrice(p.price)}</td>
              <td className="num">{p.quantity}</td>
              <td>{p.description}</td>
              {isAdmin && (
                <td className="actions">
                  <button type="button" className="btn-small btn-secondary" onClick={() => setEditing(p)}>Edit</button>
                  <button type="button" className="btn-small btn-danger" disabled={busy} onClick={() => remove(p)}>
                    Delete<span className="btn-endpoint">DELETE /api/products/{'{id}'}</span>
                  </button>
                </td>
              )}
            </tr>
          ))}
        </tbody>
      </table>
    </>
  );
}
