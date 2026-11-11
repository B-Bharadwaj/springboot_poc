import { Fragment, useEffect, useState } from 'react';
import { api, bannerMessage } from '../api.js';
import { formatDate, formatPrice } from '../format.js';
import EndpointList from '../components/EndpointList.jsx';
import ErrorBanner from '../components/ErrorBanner.jsx';

export default function OrdersPage() {
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(5);
  const [sort, setSort] = useState('createdAt,desc');
  const [data, setData] = useState(null);
  const [expanded, setExpanded] = useState(null);
  const [banner, setBanner] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    let active = true;
    setLoading(true);
    api(`/api/orders/page?page=${page}&size=${size}&sort=${sort}`)
      .then((result) => {
        if (!active) return;
        setData(result);
        setBanner(null);
      })
      .catch((err) => active && setBanner({ type: 'error', text: bannerMessage(err) }))
      .finally(() => active && setLoading(false));
    // Ignores a late response after the controls changed again.
    return () => {
      active = false;
    };
  }, [page, size, sort]);

  const totalPages = data ? data.totalPages : 0;

  return (
    <>
      <h1>Orders</h1>
      <EndpointList endpoints={[['GET', '/api/orders/page']]} />
      <ErrorBanner banner={banner} onClose={() => setBanner(null)} />

      <div className="filters">
        <div className="filter">
          <label htmlFor="size">Page size</label>
          <select id="size" value={size} onChange={(e) => { setSize(Number(e.target.value)); setPage(0); }}>
            <option value={5}>5</option>
            <option value={10}>10</option>
            <option value={20}>20</option>
          </select>
        </div>
        <div className="filter">
          <label htmlFor="sort">Sort</label>
          <select id="sort" value={sort} onChange={(e) => { setSort(e.target.value); setPage(0); }}>
            <option value="createdAt,desc">Newest first</option>
            <option value="createdAt,asc">Oldest first</option>
          </select>
        </div>
      </div>

      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>User</th>
            <th>Status</th>
            <th>Created</th>
            <th className="num">Items</th>
          </tr>
        </thead>
        <tbody>
          {data && data.content.length === 0 && (
            <tr><td colSpan="5" className="empty">No orders</td></tr>
          )}
          {data && data.content.map((order) => (
            <Fragment key={order.id}>
              <tr className="clickable" onClick={() => setExpanded(expanded === order.id ? null : order.id)}>
                <td>{expanded === order.id ? '▾' : '▸'} {order.id}</td>
                <td>{order.userName}</td>
                <td>{order.status}</td>
                <td>{formatDate(order.createdAt)}</td>
                <td className="num">{order.items.length}</td>
              </tr>
              {expanded === order.id && (
                <tr className="expanded">
                  <td colSpan="5">
                    <table className="inner">
                      <thead>
                        <tr>
                          <th>Product</th>
                          <th className="num">Quantity</th>
                          <th className="num">Unit price</th>
                        </tr>
                      </thead>
                      <tbody>
                        {order.items.map((item, i) => (
                          <tr key={i}>
                            <td>{item.productName}</td>
                            <td className="num">{item.quantity}</td>
                            <td className="num">{formatPrice(item.unitPrice)}</td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </td>
                </tr>
              )}
            </Fragment>
          ))}
        </tbody>
      </table>

      <div className="pager">
        <button type="button" className="btn-secondary" disabled={loading || page === 0}
          onClick={() => setPage(page - 1)}>
          Previous<span className="btn-endpoint">GET /api/orders/page</span>
        </button>
        <span>
          Page {totalPages === 0 ? 0 : page + 1} of {totalPages} · {data ? data.totalElements : 0} orders
        </span>
        <button type="button" className="btn-secondary" disabled={loading || page + 1 >= totalPages}
          onClick={() => setPage(page + 1)}>
          Next<span className="btn-endpoint">GET /api/orders/page</span>
        </button>
      </div>
    </>
  );
}
