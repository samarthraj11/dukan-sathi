import { useNavigate } from "react-router-dom";
import Avatar from "../components/Avatar";
import BottomNav from "../components/BottomNav";
import { distributors } from "../data";
import { useApp } from "../state";
import "./Account.css";

/** Placeholder until ledger, payments and returns are designed. */
export default function Account() {
  const { phone, signOut } = useApp();
  const navigate = useNavigate();
  const linked = distributors.filter((d) => d.status === "active");

  return (
    <>
      <header className="acct__head">
        <span className="acct__me" role="img" aria-label="Gupta General Store">
          G
        </span>
        <div>
          <h1>Gupta General Store</h1>
          <p>+91 {phone.slice(0, 5)} {phone.slice(5)}</p>
        </div>
      </header>

      <div className="acct__body">
        <h2>My distributors</h2>
        <ul>
          {linked.map((d) => (
            <li key={d.id}>
              <Avatar initial={d.initial} color={d.color} size={44} />
              <strong>{d.name}</strong>
            </li>
          ))}
        </ul>
        <p className="acct__soon">Ledger, payments and returns are coming soon.</p>
        <button
          className="btn-outline acct__logout"
          onClick={() => {
            signOut();
            navigate("/login", { replace: true });
          }}
        >
          Log out
        </button>
      </div>

      <BottomNav />
    </>
  );
}
