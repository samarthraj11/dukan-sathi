import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useApp } from "../state";
import "./PhoneScreen.css";

type Lang = "en" | "hi";

const groupDigits = (d: string) => (d.length > 5 ? `${d.slice(0, 5)} ${d.slice(5)}` : d);

export default function PhoneScreen() {
  const { phone, setPhone } = useApp();
  const navigate = useNavigate();
  const [lang, setLang] = useState<Lang>("en");

  return (
    <main className="phone">
      <div className="phone__lang" role="group" aria-label="Language">
        <button className={lang === "en" ? "is-on" : ""} onClick={() => setLang("en")} aria-pressed={lang === "en"}>
          English
        </button>
        <button className={lang === "hi" ? "is-on" : ""} onClick={() => setLang("hi")} aria-pressed={lang === "hi"}>
          हिन्दी
        </button>
      </div>

      <div className="stamp">
        <div className="stamp__en">Dukaan Saathi</div>
        <div className="stamp__hi">दुकान साथी</div>
      </div>

      <div className="phone__tagline">
        <p>Order from your distributor. No calls, no paper slips.</p>
        <p className="phone__tagline-hi">फ़ोन और पर्ची की ज़रूरत नहीं।</p>
      </div>

      <form
        className="phone__form"
        onSubmit={(e) => {
          e.preventDefault();
          if (phone.length === 10) navigate("/login/pin");
        }}
      >
        <label htmlFor="phone">Your mobile number</label>
        <div className="phone__field">
          <span className="phone__prefix">+91</span>
          <input
            id="phone"
            type="tel"
            inputMode="numeric"
            autoComplete="tel-national"
            value={groupDigits(phone)}
            onChange={(e) => setPhone(e.target.value.replace(/\D/g, "").slice(0, 10))}
            placeholder="98765 43210"
          />
        </div>
        <p className="phone__hint">Next you will set a 6-digit PIN. No OTP needed.</p>

        <div className="phone__spacer" />

        <button className="btn-primary phone__cta" disabled={phone.length !== 10}>
          Continue
        </button>
        <p className="phone__foot">Distributor? Use the number registered with us.</p>
      </form>
    </main>
  );
}
