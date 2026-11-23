import { NavLink } from "react-router-dom";
import Icon, { type IconName } from "./Icon";
import "./BottomNav.css";

const tabs: { to: string; label: string; icon: IconName; end?: boolean }[] = [
  { to: "/", label: "Home", icon: "home", end: true },
  { to: "/catalogue", label: "Catalogue", icon: "grid" },
  { to: "/orders", label: "Orders", icon: "receipt" },
  { to: "/account", label: "Account", icon: "user" },
];

export default function BottomNav() {
  return (
    <nav className="bnav" aria-label="Main">
      {tabs.map((t) => (
        <NavLink key={t.to} to={t.to} end={t.end} className="bnav__tab">
          <Icon name={t.icon} size={30} />
          {t.label}
        </NavLink>
      ))}
    </nav>
  );
}
