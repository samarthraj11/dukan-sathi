import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { distributors } from "../data";
import { useApp } from "../state";
import Avatar from "./Avatar";
import Icon from "./Icon";
import "./DistributorSheet.css";

type Props = { onClose: () => void };

/** Bottom sheet: pick which distributor the shop orders from. */
export default function DistributorSheet({ onClose }: Props) {
  const { distributorId, selectDistributor } = useApp();
  const navigate = useNavigate();

  useEffect(() => {
    const onKey = (e: KeyboardEvent) => e.key === "Escape" && onClose();
    window.addEventListener("keydown", onKey);
    return () => window.removeEventListener("keydown", onKey);
  }, [onClose]);

  return (
    <div className="dsheet" role="dialog" aria-modal="true" aria-label="Order from">
      <div className="dsheet__scrim" onClick={onClose} />
      <div className="dsheet__panel">
        <div className="dsheet__handle" />
        <h2>Order from</h2>

        <ul>
          {distributors.map((d) => {
            const selected = d.id === distributorId;
            const pending = d.status === "pending";
            return (
              <li key={d.id}>
                <button
                  className={`dsheet__row${selected ? " is-selected" : ""}${pending ? " is-pending" : ""}`}
                  disabled={pending}
                  aria-pressed={selected}
                  onClick={() => {
                    selectDistributor(d.id);
                    onClose();
                  }}
                >
                  <Avatar initial={d.initial} color={d.color} size={54} faded={pending} />
                  <span className="dsheet__text">
                    <strong>{d.name}</strong>
                    <span className={`dsheet__note dsheet__note--${d.tone}`}>
                      {d.tone === "ok" && <i className="dsheet__dot" />}
                      {d.tone === "wait" && <Icon name="clock" size={20} stroke={2.4} />}
                      {d.note}
                    </span>
                  </span>
                  {selected ? (
                    <span className="dsheet__check">
                      <Icon name="check" size={18} stroke={4} />
                    </span>
                  ) : (
                    !pending && <span className="dsheet__ring" />
                  )}
                </button>
              </li>
            );
          })}
        </ul>

        <button
          className="btn-outline dsheet__add"
          onClick={() => {
            onClose();
            navigate("/add-distributor");
          }}
        >
          <Icon name="plus" size={26} stroke={3} />
          Add distributor
        </button>
      </div>
    </div>
  );
}
