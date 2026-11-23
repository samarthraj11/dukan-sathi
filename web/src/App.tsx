import { Route, Routes } from "react-router-dom";

export default function App() {
  return (
    <div className="app">
      <Routes>
        <Route path="*" element={<p style={{ padding: 24 }}>Dukaan Saathi</p>} />
      </Routes>
    </div>
  );
}
