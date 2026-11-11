import MethodBadge from './MethodBadge.jsx';

// Grey strip under a page title listing the calls that page makes.
export default function EndpointList({ endpoints }) {
  return (
    <div className="endpoints">
      <span className="endpoints-title">Endpoints used</span>
      {endpoints.map(([method, path]) => (
        <span className="endpoint" key={`${method} ${path}`}>
          <MethodBadge method={method} /> <code>{path}</code>
        </span>
      ))}
    </div>
  );
}
