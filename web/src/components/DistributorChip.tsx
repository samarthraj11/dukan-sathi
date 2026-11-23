import type { Distributor } from "../data";
import Avatar from "./Avatar";
import Icon from "./Icon";
import "./DistributorChip.css";

type Props = { distributor: Distributor; onClick?: () => void; compact?: boolean };

/** Shows who the shop is ordering from. Tapping opens the distributor sheet. */
export default function DistributorChip({ distributor, onClick, compact = false }: Props) {
  return (
    <button
      className={compact ? "dchip dchip--compact" : "dchip"}
      onClick={onClick}
      aria-label={`Ordering from ${distributor.name}. Change distributor`}
    >
      <Avatar initial={distributor.initial} color={distributor.color} size={compact ? 34 : 38} />
      <span className="dchip__name">{distributor.name}</span>
      <Icon name="chevronDown" size={compact ? 20 : 22} stroke={2.6} />
    </button>
  );
}
