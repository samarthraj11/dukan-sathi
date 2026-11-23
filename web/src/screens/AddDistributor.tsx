import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Icon from "../components/Icon";
import "./AddDistributor.css";

const groupDigits = (d: string) => (d.length > 5 ? `${d.slice(0, 5)} ${d.slice(5)}` : d);

export default function AddDistributor() {
  const navigate = useNavigate();
  const [number, setNumber] = useState("");
  const [sent, setSent] = useState(false);

  return (
    <main className="adddist">
      <header className="adddist__head">
        <button aria-label="Back" onClick={() => navigate(-1)}>
          <Icon name="back" size={32} stroke={2.4} />
        </button>
        <h1>Add distributor</h1>
      </header>

      <form
        className="adddist__form"
        onSubmit={(e) => {
          e.preventDefault();
          if (number.length === 10) setSent(true);
        }}
      >
        <label htmlFor="dnum">Distributor's number</label>
        <div className="adddist__field">
          <span>+91</span>
          <input
            id="dnum"
            type="tel"
            inputMode="numeric"
            value={groupDigits(number)}
            placeholder="98100 12345"
            onChange={(e) => {
              setSent(false);
              setNumber(e.target.value.replace(/\D/g, "").slice(0, 10));
            }}
          />
        </div>

        <button className="btn-primary adddist__send" disabled={number.length !== 10 || sent}>
          {sent ? "Request sent" : "Send request"}
        </button>
      </form>

      <div className="adddist__or">
        <i />
        or
        <i />
      </div>

      <button className="btn-outline adddist__qr">
        <Icon name="qr" size={30} />
        Scan their QR code
      </button>

      <div className="adddist__spacer" />

      <p className="adddist__note">
        <Icon name="info" size={28} />
        {sent ? "Request sent. You can order once they approve." : "They approve you first. Then you can order."}
      </p>
    </main>
  );
}
