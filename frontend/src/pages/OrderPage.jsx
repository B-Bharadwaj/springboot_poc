import { useEffect, useRef, useState } from 'react';
import { api, bannerMessage, fieldErrorMap, hasFieldErrors } from '../api.js';
import { formatDate, formatPrice, toNumber } from '../format.js';
import EndpointList from '../components/EndpointList.jsx';
import ErrorBanner from '../components/ErrorBanner.jsx';
import FieldError from '../components/FieldError.jsx';

export default function OrderPage() {
  const nextKey = useRef(1);
  const [products, setProducts] = useState([]);
  const [lines, setLines] = useState([{ key: 0, productId: '', quantity: '1' }]);
  const [errors, setErrors] = useState({});
  const [banner, setBanner] = useState(null);
  const [busy, setBusy] = useState(false);
  const [placed, setPlaced] = useState(null);

  async function loadProducts() {
    try {
      setProducts(await api('/api/products'));
    } catch (err) {
      setBanner({ type: 'error', text: bannerMessage(err) });
    }
  }

  useEffect(() => {
    loadProducts();
  }, []);

  function updateLine(key, field, value) {
    setLines(lines.map((l) => (l.key === key ? { ...l, [field]: value } : l)));
  }

  function addLine() {
    setLines([...lines, { key: nextKey.current++, productId: '', quantity: '1' }]);
  }

  function removeLine(key) {
    setLines(lines.filter((l) => l.key !== key));
    setErrors({});
  }

  async function submit(e) {
    e.preventDefault();
    setBusy(true);
    setErrors({});
    const body = {
      items: lines.map((l) => ({ productId: toNumber(l.productId), quantity: toNumber(l.quantity) })),
    };
    try {
      const order = await api('/api/orders', { method: 'POST', body });
      setPlaced(order);
      setBanner(null);
      setLines([{ key: nextKey.current++, productId: '', quantity: '1' }]);
      loadProducts();
    } catch (err) {
      setPlaced(null);
      if (hasFieldErrors(err)) {
        setErrors(fieldErrorMap(err));
      } else {
        setBanner({ type: 'error', text: bannerMessage(err) });
        if (err.status === 404) loadProducts();
      }
    } finally {
      setBusy(false);
    }
  }

  return (
    <>
      <h1>Place order</h1>
      <EndpointList endpoints={[['GET', '/api/products'], ['POST', '/api/orders']]} />
      <ErrorBanner banner={banner} onClose={() => setBanner(null)} />

      {placed && (
        <div className="card confirm">
          <h2>Order #{placed.id} placed</h2>
          <p>
            Status: {placed.status} · {formatDate(placed.createdAt)} · for {placed.userName}
          </p>
          <ItemsTable items={placed.items} />
        </div>
      )}

      <form className="card" onSubmit={submit} noValidate>
        <p className="hint">At least one line; each needs a product and a quantity of 1 or more.</p>
        <FieldError message={errors.items} />
        {lines.map((line, i) => (
          <div className="order-line" key={line.key}>
            <span className="line-number">Line {i + 1}</span>
            <div>
              <label htmlFor={`product-${line.key}`}>Product</label>
              <select id={`product-${line.key}`} value={line.productId}
                onChange={(e) => updateLine(line.key, 'productId', e.target.value)}>
                <option value="">Choose a product</option>
                {products.map((p) => (
                  <option key={p.id} value={p.id}>
                    {p.name} (stock {p.quantity})
                  </option>
                ))}
              </select>
              <FieldError message={errors[`items[${i}].productId`]} />
            </div>
            <div>
              <label htmlFor={`qty-${line.key}`}>Quantity</label>
              <input id={`qty-${line.key}`} type="number" step="1" value={line.quantity}
                onChange={(e) => updateLine(line.key, 'quantity', e.target.value)} />
              <FieldError message={errors[`items[${i}].quantity`]} />
            </div>
            <button type="button" className="btn-small btn-secondary" onClick={() => removeLine(line.key)}>
              Remove
            </button>
          </div>
        ))}
        <div className="row">
          <button type="button" className="btn-secondary" onClick={addLine}>Add line</button>
          <button type="submit" disabled={busy}>
            {busy ? 'Placing…' : 'Place order'}
            <span className="btn-endpoint">POST /api/orders</span>
          </button>
        </div>
      </form>
    </>
  );
}

function ItemsTable({ items }) {
  return (
    <table className="inner">
      <thead>
        <tr>
          <th>Product</th>
          <th className="num">Quantity</th>
          <th className="num">Unit price</th>
        </tr>
      </thead>
      <tbody>
        {items.map((item, i) => (
          <tr key={i}>
            <td>{item.productName}</td>
            <td className="num">{item.quantity}</td>
            <td className="num">{formatPrice(item.unitPrice)}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
