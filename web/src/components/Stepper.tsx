import "./Stepper.css";

type Props = { qty: number; onChange: (qty: number) => void; name: string };

/** Carton stepper. Big targets, no typing. */
export default function Stepper({ qty, onChange, name }: Props) {
  return (
    <div className="stepper" role="group" aria-label={`${name} cartons`}>
      <button aria-label={`Remove one carton of ${name}`} onClick={() => onChange(qty - 1)}>
        −
      </button>
      <span aria-live="polite">{qty}</span>
      <button aria-label={`Add one carton of ${name}`} className="stepper__plus" onClick={() => onChange(qty + 1)}>
        +
      </button>
    </div>
  );
}
