import { useNavigate } from "react-router-dom";
import BottomNav from "../components/BottomNav";
import DistributorSwitcher from "../components/DistributorSwitcher";
import Icon from "../components/Icon";
import ProductArt from "../components/ProductArt";
import { distributors, formatRupees, lastOrderItems, products, usualOrder, type Order } from "../data";
import { priceItems, useApp } from "../state";
import "./Home.css";

const STEP: Record<Order["status"], number> = { Placed: 1, "On the way": 4, Delivered: 5 };

export default function Home() {
  const { distributorId, orders, fillCart, replaceCart } = useApp();
  const navigate = useNavigate();
  const distributor = distributors.find((d) => d.id === distributorId)!;

  const active = orders.find((o) => o.distributorId === distributorId && o.status !== "Delivered");
  const last = priceItems(lastOrderItems[distributorId] ?? {});
  const usual = Object.entries(usualOrder[distributorId] ?? {}).map(([id, qty]) => ({
    product: products.find((p) => p.id === id)!,
    qty,
  }));

  return (
    <>
      <header className="home__hero">
        <div className="home__top">
          <span className="home__me" role="img" aria-label="Gupta General Store">
            G
          </span>
          <DistributorSwitcher />
        </div>

        <div className="home__status">
          {active ? (
            <>
              <p className="home__label">Order {active.number}</p>
              <h1>{active.status}</h1>
              <div className="home__bar" role="img" aria-label={`Step ${STEP[active.status]} of 5`}>
                {[1, 2, 3, 4, 5].map((n) => (
                  <span key={n} className={n <= STEP[active.status] ? "is-on" : ""} />
                ))}
              </div>
            </>
          ) : (
            <>
              <p className="home__label">{distributor.name}</p>
              <h1>Nothing on the way</h1>
            </>
          )}
          <div className="home__row">
            <p className="home__eta">{active ? (active.status === "Placed" ? "Waiting to confirm" : "Arriving by 4 PM") : "Ready to order"}</p>
            <button className="home__call" aria-label={`Call ${distributor.name}`}>
              <Icon name="phone" size={24} />
            </button>
          </div>
        </div>
      </header>

      <div className="home__body">
        <button
          className="home__again"
          onClick={() => {
            replaceCart(lastOrderItems[distributorId] ?? {});
            navigate("/cart");
          }}
          disabled={last.cartons === 0}
        >
          <span>
            <strong>Order again</strong>
            <small>
              {last.cartons} cartons, {formatRupees(last.total)}
            </small>
          </span>
          <Icon name="repeat" size={34} stroke={2.4} />
        </button>

        <section>
          <div className="home__usual-head">
            <h2>Monday usual</h2>
            <button
              className="btn-outline home__addall"
              onClick={() => {
                fillCart(usualOrder[distributorId] ?? {});
                navigate("/cart");
              }}
            >
              Add all
            </button>
          </div>
          <ul className="home__usual">
            {usual.map(({ product, qty }) => (
              <li key={product.id}>
                <div className="home__tile">
                  <ProductArt art={product.art} />
                  <span className="home__badge">{qty}</span>
                </div>
                <span>{product.name}</span>
              </li>
            ))}
          </ul>
        </section>
      </div>

      <BottomNav />
    </>
  );
}

