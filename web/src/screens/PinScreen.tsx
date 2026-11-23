import { useState } from "react";
import { Navigate, useNavigate } from "react-router-dom";
import Icon from "../components/Icon";
import { Keypad, PinBlanks, PIN_LENGTH } from "../components/PinPad";
import { demoPin, knownPhones } from "../data";
import { useApp } from "../state";
import "./PinScreen.css";

const prettyPhone = (p: string) => `+91 ${p.slice(0, 5)} ${p.slice(5)}`;

export default function PinScreen() {
  const { phone, signIn } = useApp();
  const navigate = useNavigate();
  const [pin, setPin] = useState("");
  const [error, setError] = useState("");

  if (phone.length !== 10) return <Navigate to="/login" replace />;

  const returning = knownPhones.includes(phone);

  const submit = () => {
    if (pin.length !== PIN_LENGTH) return;
    if (returning && pin !== demoPin) {
      setError("Wrong PIN. Try again.");
      setPin("");
      return;
    }
    signIn();
    navigate("/", { replace: true });
  };

  return (
    <main className="pin">
      <button className="pin__back" aria-label="Back" onClick={() => navigate("/login")}>
        <Icon name="back" size={32} stroke={2.4} />
      </button>

      <h1>{returning ? "Welcome back" : "Create your PIN"}</h1>
      <p className="pin__lead">
        {returning
          ? "Enter your 6-digit PIN."
          : "Pick 6 digits you will remember. You will enter it every time you open the app."}
      </p>

      <div className="pin__number">
        <span>{prettyPhone(phone)}</span>
        <button onClick={() => navigate("/login")}>Change</button>
      </div>

      <div className="pin__blanks">
        <PinBlanks value={pin} />
      </div>

      {error && (
        <p className="pin__error" role="alert">
          {error}
        </p>
      )}

      {returning && (
        <div className="pin__forgot">
          <button>Forgot PIN?</button>
          <p>Ask your distributor to reset it.</p>
        </div>
      )}

      <div className="pin__spacer" />

      <button className="btn-primary pin__cta" disabled={pin.length !== PIN_LENGTH} onClick={submit}>
        {returning ? "Login" : "Set PIN and continue"}
      </button>
      <Keypad
        onChange={(update) => {
          setError("");
          setPin(update);
        }}
      />
    </main>
  );
}
