import { useState } from 'react';
import { api, fieldErrorMap, hasFieldErrors } from '../api.js';
import { toNumber } from '../format.js';
import FieldError from './FieldError.jsx';

// One form for both Add (product is null) and Edit.
export default function ProductForm({ product, onSaved, onCancel, onError }) {
  const [form, setForm] = useState({
    name: product ? product.name : '',
    price: product ? String(product.price) : '',
    quantity: product ? String(product.quantity) : '',
    description: product && product.description ? product.description : '',
  });
  const [errors, setErrors] = useState({});
  const [busy, setBusy] = useState(false);

  function change(field) {
    return (e) => setForm({ ...form, [field]: e.target.value });
  }

  async function submit(e) {
    e.preventDefault();
    setBusy(true);
    setErrors({});
    const body = {
      name: form.name,
      price: toNumber(form.price),
      quantity: toNumber(form.quantity),
      description: form.description,
    };
    try {
      const saved = product
        ? await api(`/api/products/${product.id}`, { method: 'PUT', body })
        : await api('/api/products', { method: 'POST', body });
      onSaved(saved);
    } catch (err) {
      if (hasFieldErrors(err)) setErrors(fieldErrorMap(err));
      else onError(err);
      setBusy(false);
    }
  }

  return (
    <form className="card" onSubmit={submit} noValidate>
      <h2>{product ? `Edit product #${product.id}` : 'Add product'}</h2>

      <label htmlFor="p-name">Name <span className="hint">(required, max 100)</span></label>
      <input id="p-name" value={form.name} onChange={change('name')} />
      <FieldError message={errors.name} />

      <label htmlFor="p-price">Price <span className="hint">(greater than 0)</span></label>
      <input id="p-price" type="number" step="0.01" value={form.price} onChange={change('price')} />
      <FieldError message={errors.price} />

      <label htmlFor="p-quantity">Quantity <span className="hint">(0 or more)</span></label>
      <input id="p-quantity" type="number" step="1" value={form.quantity} onChange={change('quantity')} />
      <FieldError message={errors.quantity} />

      <label htmlFor="p-description">Description <span className="hint">(max 500)</span></label>
      <textarea id="p-description" rows="3" value={form.description} onChange={change('description')} />
      <FieldError message={errors.description} />

      <div className="row">
        <button type="submit" disabled={busy}>
          {busy ? 'Saving…' : 'Save'}
          <span className="btn-endpoint">{product ? 'PUT /api/products/{id}' : 'POST /api/products'}</span>
        </button>
        <button type="button" className="btn-secondary" onClick={onCancel} disabled={busy}>Cancel</button>
      </div>
    </form>
  );
}
