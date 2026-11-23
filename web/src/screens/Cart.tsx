import { useNavigate } from "react-router-dom";
import Avatar from "../components/Avatar";
import Icon from "../components/Icon";
import ProductArt from "../components/ProductArt";
import Stepper from "../components/Stepper";
import { distributors, formatRupees, OFFER_THRESHOLD, products } from "../data";
import { cartTotals, useApp } from "../state";
import "./Cart.css";

export default function Cart() {
  const { distributorId, cart, setQty, placeOrder } = useApp();
  const navigate = useNavigate();
  const distributor = distributors.find((d) => d.id === distributorId)!;
  const { lines, subtotal, gst, total } = cartTotals(distributorId, cart);
  const hasOffer = products.some((p) => p.distributorId === distributorId && p.offer);
  const toGo = OFFER_THRESHOLD - subtotal;

  return (
    <main className="cart">
      <header className="cart__head">
        <div>
          <button aria-label="Back to catalogue" onClick={() => navigate("/catalogue")}>
            <Icon name="back" size={32} stroke={2.4} />
          </button>
          <h1>Cart</h1>
        </div>
        <span className="cart__who">
          <Avatar initial={distributor.initial} color={distributor.color} size={30} />
          {distributor.name.split(" ")[0]}
        </span>
      </header>

      {lines.length === 0 ? (
        <div className="cart__empty">
          <p>Your cart is empty.</p>
          <button className="btn-primary" onClick={() => navigate("/catalogue")}>
            Open catalogue
          </button>
        </div>
      ) : (
        <>
          <ul className="cart__lines">
            {lines.map(({ product, qty, total: lineTotal }) => (
              <li key={product.id}>
                <ProductArt art={product.art} size={60} />
                <div className="cart__info">
                  <strong>{product.name}</strong>
                  <span>{formatRupees(lineTotal)}</span>
                </div>
                <Stepper qty={qty} name={product.name} onChange={(q) => setQty(product.id, q)} />
              </li>
            ))}
          </ul>

          {hasOffer && (
            <p className="cart__offer">
              <Icon name="star" size={28} filled />
              {toGo > 0 ? `Add ${formatRupees(toGo)} more, get 1 carton free` : "1 carton free added"}
            </p>
          )}

          <dl className="cart__sums">
            <div>
              <dt>GST 18%</dt>
              <dd>{formatRupees(gst)}</dd>
            </div>
            <div className="cart__total">
              <dt>Total</dt>
              <dd>{formatRupees(total)}</dd>
            </div>
          </dl>

          <div className="cart__chips">
            <span className="cart__chip cart__chip--green">
              <Icon name="truck" size={26} />
              Tue 25 Nov
            </span>
            <span className="cart__chip">
              <Icon name="wallet" size={26} />
              On delivery
            </span>
          </div>

          <div className="cart__spacer" />

          <button
            className="btn-primary cart__cta"
            onClick={() => {
              const order = placeOrder();
              if (order) navigate("/placed", { state: { orderId: order.id } });
            }}
          >
            Place order
          </button>
        </>
      )}
    </main>
  );
}
