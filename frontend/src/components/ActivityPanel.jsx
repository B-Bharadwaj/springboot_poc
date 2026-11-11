import { useSyncExternalStore } from 'react';
import { clearActivity, getActivity, subscribeActivity } from '../api.js';
import useResizablePanel from '../hooks/useResizablePanel.js';
import MethodBadge from './MethodBadge.jsx';

function statusClass(status) {
  if (status >= 200 && status < 300) return 'status-ok';
  if (status >= 400 && status < 500) return 'status-warn';
  if (status === 0 || status >= 500) return 'status-fail';
  return '';
}

// Pinned, resizable panel showing every API call, newest first.
export default function ActivityPanel() {
  const entries = useSyncExternalStore(subscribeActivity, getActivity);
  const { panelRef, handleProps, collapsed, toggle } = useResizablePanel({ minHeight: 36, defaultHeight: 220 });

  return (
    <section ref={panelRef} className="activity" aria-label="API activity">
      <div className="activity-handle" aria-label="Resize API activity panel" {...handleProps} />
      <div className="activity-header">
        <strong>API calls ({entries.length})</strong>
        <button type="button" className="btn-small btn-secondary" onClick={clearActivity}>Clear</button>
        <button type="button" className="btn-small btn-secondary activity-chevron" onClick={toggle}
          aria-expanded={!collapsed} aria-label={collapsed ? 'Expand API activity panel' : 'Collapse API activity panel'}>
          {collapsed ? '▴' : '▾'}
        </button>
      </div>
      <div className="activity-body">
        <table>
          <thead>
            <tr>
              <th>Time</th>
              <th>Method</th>
              <th>Endpoint</th>
              <th>Status</th>
              <th className="num">ms</th>
              <th>Error</th>
            </tr>
          </thead>
          <tbody>
            {entries.length === 0 && (
              <tr><td colSpan="6" className="empty">No calls yet</td></tr>
            )}
            {entries.map((e) => (
              <tr key={e.id}>
                <td>{e.time.toLocaleTimeString()}</td>
                <td><MethodBadge method={e.method} /></td>
                <td><code>{e.path}</code></td>
                <td className={statusClass(e.status)}>{e.status}</td>
                <td className="num">{Math.round(e.ms)}</td>
                <td>{e.error || ''}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </section>
  );
}
