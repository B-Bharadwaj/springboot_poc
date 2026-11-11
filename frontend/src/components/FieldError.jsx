// Small red text under an input; renders nothing without a message.
export default function FieldError({ message }) {
  return message ? <div className="field-error">{message}</div> : null;
}
