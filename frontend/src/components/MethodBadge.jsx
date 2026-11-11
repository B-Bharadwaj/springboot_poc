// Coloured HTTP method label: GET green, POST blue, PUT orange, DELETE red.
export default function MethodBadge({ method }) {
  return <span className={`method method-${method.toLowerCase()}`}>{method}</span>;
}
