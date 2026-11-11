import { useState } from 'react';
import { api, bannerMessage } from '../api.js';
import EndpointList from '../components/EndpointList.jsx';
import ErrorBanner from '../components/ErrorBanner.jsx';

const ERROR_DEMOS = [
  {
    key: 'delete',
    label: 'Delete product 999999',
    endpoint: ['DELETE', '/api/products/999999'],
    expect: 'USER gets 403, ADMIN gets 404',
    run: () => api('/api/products/999999', { method: 'DELETE' }),
  },
  {
    key: 'sort',
    label: 'Page with an invalid sort',
    endpoint: ['GET', '/api/orders/page?sort=nonsense'],
    expect: '400',
    run: () => api('/api/orders/page?sort=nonsense'),
  },
  {
    key: 'noToken',
    label: 'Call products with no token',
    endpoint: ['GET', '/api/products'],
    expect: '401, your login is kept',
    // auth: false sends no header and skips the logout-on-401 rule.
    run: () => api('/api/products', { auth: false }),
  },
];

const ENDPOINTS = [['GET', '/api/orders/slow'], ['GET', '/api/orders/fast'], ...ERROR_DEMOS.map((d) => d.endpoint)];

export default function DemoPage() {
  const [banner, setBanner] = useState(null);
  const [timings, setTimings] = useState({});
  const [results, setResults] = useState({});
  const [busy, setBusy] = useState(null);

  async function timeCall(which) {
    setBusy(which);
    const start = performance.now();
    try {
      const orders = await api(`/api/orders/${which}`);
      const ms = performance.now() - start;
      setTimings((t) => ({ ...t, [which]: { count: orders.length, ms } }));
      setBanner(null);
    } catch (err) {
      setBanner({ type: 'error', text: bannerMessage(err) });
    } finally {
      setBusy(null);
    }
  }

  async function runDemo(demo) {
    setBusy(demo.key);
    try {
      await demo.run();
      setResults((r) => ({ ...r, [demo.key]: 'Succeeded, no error' }));
    } catch (err) {
      setResults((r) => ({ ...r, [demo.key]: `${err.status}: ${bannerMessage(err)}` }));
    } finally {
      setBusy(null);
    }
  }

  return (
    <>
      <h1>Demo tools</h1>
      <EndpointList endpoints={ENDPOINTS} />
      <ErrorBanner banner={banner} onClose={() => setBanner(null)} />

      <section className="card">
        <h2>Slow vs fast</h2>
        <p className="hint">ADMIN only. slow = lazy loading (more queries), fast = join fetch (one query).</p>
        {['slow', 'fast'].map((which) => (
          <div className="demo-row" key={which}>
            <button type="button" disabled={busy !== null} onClick={() => timeCall(which)}>
              Run {which}<span className="btn-endpoint">GET /api/orders/{which}</span>
            </button>
            <span>
              {timings[which]
                ? `${timings[which].count} orders in ${timings[which].ms.toFixed(1)} ms`
                : 'Not run yet'}
            </span>
          </div>
        ))}
      </section>

      <section className="card">
        <h2>Error demos</h2>
        {ERROR_DEMOS.map((demo) => (
          <div className="demo-row" key={demo.key}>
            <button type="button" className="btn-secondary" disabled={busy !== null} onClick={() => runDemo(demo)}>
              {demo.label}
              <span className="btn-endpoint">{demo.endpoint.join(' ')}</span>
            </button>
            <span className="hint">Expect {demo.expect}</span>
            <strong>{results[demo.key] || ''}</strong>
          </div>
        ))}
      </section>
    </>
  );
}
