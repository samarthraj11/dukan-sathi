import { useState } from "react";
import { useNavigate } from "react-router-dom";
import BottomNav from "../components/BottomNav";
import DistributorSwitcher from "../components/DistributorSwitcher";
import Icon from "../components/Icon";
import ProductArt from "../components/ProductArt";
import Stepper from "../components/Stepper";
import { formatRupees, products, usualOrder } from "../data";
import { cartTotals, useApp } from "../state";
import "./Catalogue.css";

type Filter = "all" | "usual" | "offers";

export default function Catalogue() {
  const { distributorId, cart, setQty } = useApp();
  const navigate = useNavigate();
  const [query, setQuery] = useState("");
  const [filter, setFilter] = useState<Filter>("all");

  const usualIds = Object.keys(usualOrder[distributorId] ?? {});
  const items = products
    .filter((p) => p.distributorId === distributorId)
    .filter((p) => (filter === "usual" ? usualIds.includes(p.id) : filter === "offers" ? !!p.offer : true))
    .filter((p) => p.name.toLowerCase().includes(query.trim().toLowerCase()));

  const { cartons, subtotal } = cartTotals(distributorId, cart);

  return (
    <>
      <header className="cat__head">
        <div className="cat__switch">
          <DistributorSwitcher compact />
        </div>
        <div className="cat__search">
          <Icon name="search" size={26} />
          <input
            id="search"
            type="search"
            placeholder="Search"
            aria-label="Search products"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
          />
        </div>
      </header>

      <div className="cat__chips" role="group" aria-label="Filter">
        <button className={filter === "all" ? "is-on" : ""} onClick={() => setFilter("all")}>
          All
        </button>
        <button className={filter === "usual" ? "is-on" : ""} onClick={() => setFilter("usual")}>
          Usual
        </button>
        <button className={`cat__offers${filter === "offers" ? " is-on" : ""}`} onClick={() => setFilter("offers")}>
          Offers
        </button>
      </div>

      <div className="cat__scroll">
        <ul className="cat__list">
          {items.map((p) => {
            const qty = cart[distributorId]?.[p.id] ?? 0;
            return (
              <li key={p.id} className="cat__row">
                <ProductArt art={p.art} size={72} />
                <div className="cat__info">
                  <strong>{p.name}</strong>
                  <span className="cat__price">{formatRupees(p.price)}</span>
                  <span className="cat__meta">
                    {p.stock === "in" && <i className="cat__dot" />}
                    {p.stock === "low" && (
                      <span className="cat__low">
                        <i className="cat__dot cat__dot--low" />
                        {p.left} left
                      </span>
                    )}
                    {p.offer && <em>{p.offer}</em>}
                  </span>
                </div>
                {qty > 0 ? (
                  <Stepper qty={qty} name={p.name} onChange={(q) => setQty(p.id, q)} />
                ) : (
                  <button className="cat__add" onClick={() => setQty(p.id, 1)} aria-label={`Add ${p.name}`}>
                    Add
                  </button>
                )}
              </li>
            );
          })}
          {items.length === 0 && <li className="cat__empty">Nothing found. Try another search.</li>}
        </ul>

        {cartons > 0 && (
          <div className="cat__bar">
            <div>
              <span>{cartons} cartons</span>
              <strong>{formatRupees(subtotal)}</strong>
            </div>
            <button onClick={() => navigate("/cart")}>View cart</button>
          </div>
        )}
      </div>

      <BottomNav />
    </>
  );
}
