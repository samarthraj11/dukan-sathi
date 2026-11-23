import { useEffect } from "react";
import Icon from "./Icon";
import "./PinPad.css";

export const PIN_LENGTH = 6;

/** Six writing lines; a dot appears on each digit entered. */
export function PinBlanks({ value }: { value: string }) {
  return (
    <div className="pinblanks" role="img" aria-label={`${value.length} of ${PIN_LENGTH} digits entered`}>
      {Array.from({ length: PIN_LENGTH }, (_, i) => (
        <div key={i} className={i < value.length ? "pinblanks__slot is-filled" : "pinblanks__slot"}>
          {i < value.length && <span />}
        </div>
      ))}
    </div>
  );
}

/** Calculator-style keypad. Also listens to the physical keyboard. */
type Update = (prev: string) => string;

export function Keypad({ onChange }: { onChange: (update: Update) => void }) {
  // Updaters, so quick taps never overwrite each other.
  const press = (d: string) => onChange((v) => (v.length < PIN_LENGTH ? v + d : v));
  const del = () => onChange((v) => v.slice(0, -1));

  useEffect(() => {
    const onKey = (e: KeyboardEvent) => {
      if (/^\d$/.test(e.key)) press(e.key);
      else if (e.key === "Backspace") del();
    };
    window.addEventListener("keydown", onKey);
    return () => window.removeEventListener("keydown", onKey);
  });

  return (
    <div className="keypad">
      {["1", "2", "3", "4", "5", "6", "7", "8", "9"].map((d) => (
        <button key={d} type="button" className="keypad__key" onClick={() => press(d)}>
          {d}
        </button>
      ))}
      <div className="keypad__blank" />
      <button type="button" className="keypad__key" onClick={() => press("0")}>
        0
      </button>
      <button type="button" className="keypad__key keypad__key--alt" aria-label="Delete last digit" onClick={del}>
        <Icon name="backspace" size={34} stroke={1.9} />
      </button>
    </div>
  );
}
