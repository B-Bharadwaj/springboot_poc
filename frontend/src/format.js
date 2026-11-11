export function formatPrice(value) {
  return value == null ? '' : Number(value).toFixed(2);
}

export function formatDate(value) {
  return value ? new Date(value).toLocaleString() : '';
}

// Empty input becomes null so the backend reports "required" instead of 0.
export function toNumber(value) {
  return value === '' || value == null ? null : Number(value);
}
