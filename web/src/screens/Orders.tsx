import { useState } from "react";
import Avatar from "../components/Avatar";
import BottomNav from "../components/BottomNav";
import { distributors, formatRupees } from "../data";
import { useApp } from "../state";
import "./Orders.css";

const pillClass = { Placed: "pill--placed", "On the way": "pill--way", Delivered: "pill--done" } as const;

/** Orders from every distributor in one list, each tagged with who and how much. */
export default function Orders() {
  const { orders } = useApp();
  const [filter, setFilter] = useState<string>("all");

  const active = distributors.filter((d) => d.status === "active");
  const monthTotal = orders.reduce((sum, o) => sum + o.amount, 0);
  const byDistributor = active
    .map((d) => ({ d, total: orders.filter((o) => o.distributorId === d.id).reduce((s, o) => s + o.amount, 0) }))
    .filter((x) => x.total > 0);
  const shown = orders.filter((o) => filter === "all" || o.distributorId === filter);

  return (
    <>
      <header className="ord__head">
        <h1>Orders</h1>
        <p className="ord__label">This month</p>
        <p className="ord__total">{formatRupees(monthTotal)}</p>
        <div
          className="ord__split"
          role="img"
          aria-label={byDistributor.map((x) => `${x.d.name} ${formatRupees(x.total)}`).join(", ")}
        >
          {byDistributor.map((x) => (
            <span key={x.d.id} style={{ flex: x.total, background: x.d.color }} />
          ))}
        </div>
        <div className="ord__legend">
          {byDistributor.map((x) => (
            <span key={x.d.id}>
              <i style={{ background: x.d.color }} />
              {x.d.name.split(" ")[0]} {formatRupees(x.total)}
            </span>
          ))}
        </div>
      </header>

      <div className="ord__chips" role="group" aria-label="Filter by distributor">
        <button className={filter === "all" ? "is-on" : ""} onClick={() => setFilter("all")}>
          All
        </button>
        {active.map((d) => (
          <button key={d.id} className={filter === d.id ? "is-on" : ""} onClick={() => setFilter(d.id)}>
            <Avatar initial={d.initial} color={d.color} size={32} />
            {d.name.split(" ")[0]}
          </button>
        ))}
      </div>

      <ul className="ord__list">
        {shown.map((o) => {
          const d = distributors.find((x) => x.id === o.distributorId)!;
          return (
            <li key={o.id}>
              <Avatar initial={d.initial} color={d.color} size={50} />
              <div className="ord__who">
                <strong>{d.name}</strong>
                <span>
                  {o.number}, {o.date.toLowerCase() === "today" ? "today" : o.date}
                </span>
              </div>
              <div className="ord__amount">
                <strong>{formatRupees(o.amount)}</strong>
                <span className={`pill ${pillClass[o.status]}`}>{o.status}</span>
              </div>
            </li>
          );
        })}
      </ul>

      <BottomNav />
    </>
  );
}
