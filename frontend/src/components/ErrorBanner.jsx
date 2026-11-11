// Red (error) or green (success) message with a close button.
export default function ErrorBanner({ banner, onClose }) {
  if (!banner) return null;
  return (
    <div className={`banner banner-${banner.type}`} role={banner.type === 'error' ? 'alert' : 'status'}>
      <span>{banner.text}</span>
      <button type="button" className="banner-close" aria-label="Dismiss" onClick={onClose}>
        ×
      </button>
    </div>
  );
}
