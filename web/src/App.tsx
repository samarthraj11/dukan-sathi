import { Navigate, Outlet, Route, Routes } from "react-router-dom";
import { useApp } from "./state";
import AddDistributor from "./screens/AddDistributor";
import Home from "./screens/Home";
import PhoneScreen from "./screens/PhoneScreen";
import PinScreen from "./screens/PinScreen";

/** Signed-in screens only; everyone else goes to login. */
function Private() {
  const { signedIn } = useApp();
  return signedIn ? <Outlet /> : <Navigate to="/login" replace />;
}

export default function App() {
  const { signedIn } = useApp();
  return (
    <div className="app">
      <Routes>
        <Route path="/login" element={<PhoneScreen />} />
        <Route path="/login/pin" element={<PinScreen />} />
        <Route element={<Private />}>
          <Route path="/" element={<Home />} />
          <Route path="/add-distributor" element={<AddDistributor />} />
        </Route>
        <Route path="*" element={<Navigate to={signedIn ? "/" : "/login"} replace />} />
      </Routes>
    </div>
  );
}
