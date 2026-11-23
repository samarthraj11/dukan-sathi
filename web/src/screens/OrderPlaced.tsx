import { Navigate, useLocation, useNavigate } from "react-router-dom";
import Avatar from "../components/Avatar";
import Icon from "../components/Icon";
import { distributors, formatRupees } from "../data";
import { useApp } from "../state";
import "./OrderPlaced.css";

const placedTime = new Date().toLocaleTimeString("en-IN", { hour: "numeric", minute: "2-digit" }).toUpperCase();

const steps = [
  { name: "Placed", note: `${placedTime}`, state: "done" },
  { name: "Confirmed", note: "Waiting", state: "now" },
  { name: "Packed", note: "", state: "todo" },
  { name: "On the way", note: "", state: "todo" },
  { name: "Delivered", note: "Tue 25 Nov", state: "todo" },
] as const;

export default function OrderPlaced() {
  const { orders } = useApp();
  const navigate = useNavigate();
  const location = useLocation();
  const orderId = (location.state as { orderId?: string } | null)?.orderId;
  const order = orders.find((o) => o.id === orderId);

  if (!order) return <Navigate to="/" replace />;
  const distributor = distributors.find((d) => d.id === order.distributorId)!;

  return (
    <main className="placed">
      <header className="placed__hero">
        <span className="placed__check">
          <Icon name="check" size={42} stroke={3.2} />
        </span>
        <h1>Order placed</h1>
        <div className="placed__meta">
          <span>
            <Avatar initial={distributor.initial} color={distributor.color} size={34} />
            {order.number}
          </span>
          <span>{formatRupees(order.amount)}</span>
        </div>
      </header>

      <ol className="placed__steps">
        {steps.map((s, i) => (
          <li key={s.name} className={`placed__step is-${s.state}`}>
            <div className="placed__rail">
              <span className="placed__node">{s.state === "done" && <Icon name="check" size={18} stroke={4} />}</span>
              {i < steps.length - 1 && <span className="placed__line" />}
            </div>
            <div className="placed__text">
              <strong>{s.name}</strong>
              {s.note && <span>{s.note}</span>}
            </div>
          </li>
        ))}
      </ol>

      <div className="placed__actions">
        <button className="btn-primary" onClick={() => navigate("/", { replace: true })}>
          Done
        </button>
        <button className="btn-outline" onClick={() => navigate("/orders")}>
          Edit order
        </button>
      </div>
    </main>
  );
}
