import { useState } from 'react';
import { Link } from 'react-router-dom';
import { counts, journey, sections } from '../data/overview.js';
import './Overview.css';

const MIDDLE_STEPS = ['@Valid', 'Controller', 'Service'];

function rowText(item) {
  return [item.feature, item.what, ...item.where.map((w) => w.label)].join(' ').toLowerCase();
}

function WhereLink({ link }) {
  if (link.to) return <Link to={link.to} className="ov-button">{link.label}</Link>;
  if (link.href) {
    return (
      <a href={link.href} target="_blank" rel="noopener noreferrer" className="ov-external">
        {link.label} <span aria-label="opens in a new tab">↗</span>
      </a>
    );
  }
  return <span className="ov-note">{link.note}</span>;
}

export default function Overview() {
  const [open, setOpen] = useState(() => Object.fromEntries(sections.map((s) => [s.id, true])));
  const [filter, setFilter] = useState('');
  const query = filter.trim().toLowerCase();

  function setAll(value) {
    setOpen(Object.fromEntries(sections.map((s) => [s.id, value])));
  }

  return (
    <div id="top">
      <h1>What is implemented in the backend</h1>
      <p className="ov-subtitle">A map of every backend feature in this POC and where to see it working.</p>

      <div className="ov-glance" aria-label="At a glance">
        {counts.map((c) => (
          <div className="ov-counter" key={c.label}>
            <span className="ov-count">{c.value}</span>
            <span className="ov-count-label">{c.label}</span>
          </div>
        ))}
      </div>

      <section className="card" id="journey">
        <h2>Request journey</h2>
        <ol className="ov-journey">
          {journey.map((step) => (
            <li key={step} className={MIDDLE_STEPS.includes(step) ? 'ov-step ov-step-middle' : 'ov-step'}>
              {step}
            </li>
          ))}
        </ol>
        <div className="ov-handler">
          <span className="ov-handler-arrow">↑</span>
          Exception handler (@RestControllerAdvice): errors from @Valid, the controller and the service become the error JSON
        </div>
      </section>

      <nav className="card" aria-label="Contents">
        <h2>Contents</h2>
        <ol className="ov-toc">
          {sections.map((s) => (
            <li key={s.id}><a href={`#${s.id}`}>{s.title}</a></li>
          ))}
        </ol>
      </nav>

      <div className="ov-tools">
        <button type="button" className="btn-secondary" onClick={() => setAll(true)}>Expand all</button>
        <button type="button" className="btn-secondary" onClick={() => setAll(false)}>Collapse all</button>
        <label htmlFor="ov-filter">Filter rows</label>
        <input id="ov-filter" value={filter} placeholder="e.g. JWT" onChange={(e) => setFilter(e.target.value)} />
      </div>

      {sections.map((section, i) => {
        const rows = section.items.filter((item) => !query || rowText(item).includes(query));
        const future = section.id === 'not-implemented';
        return (
          <details
            key={section.id}
            id={section.id}
            className={future ? 'card ov-section ov-future' : 'card ov-section'}
            open={open[section.id]}
            onToggle={(e) => {
              const isOpen = e.currentTarget.open;
              setOpen((o) => (o[section.id] === isOpen ? o : { ...o, [section.id]: isOpen }));
            }}
          >
            <summary>
              <h2>{i + 1}. {section.title}{future && <span className="ov-future-tag">Future work</span>}</h2>
              <p className="ov-summary">{section.summary}</p>
            </summary>
            <table>
              <thead>
                <tr>
                  <th>Feature</th>
                  <th>What it does</th>
                  <th>Where to see it</th>
                </tr>
              </thead>
              <tbody>
                {rows.length === 0 && (
                  <tr><td colSpan="3" className="empty">No matching rows</td></tr>
                )}
                {rows.map((item) => (
                  <tr key={item.feature}>
                    <td className="ov-feature">{item.feature}</td>
                    <td>{item.what}</td>
                    <td className="ov-where">
                      {item.where.map((link) => <WhereLink key={link.label} link={link} />)}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
            <a href="#top" className="ov-top">Back to top</a>
          </details>
        );
      })}
    </div>
  );
}
