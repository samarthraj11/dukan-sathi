import { Navigate, Route, Routes } from "react-router-dom";
import { useApp } from "./state";
import PhoneScreen from "./screens/PhoneScreen";

export default function App() {
  const { signedIn } = useApp();
  return (
    <div className="app">
      <Routes>
        <Route path="/login" element={<PhoneScreen />} />
        <Route path="*" element={<Navigate to={signedIn ? "/" : "/login"} replace />} />
      </Routes>
    </div>
  );
}
