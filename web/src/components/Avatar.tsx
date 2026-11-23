type Props = { initial: string; color: string; size?: number; faded?: boolean };

export default function Avatar({ initial, color, size = 38, faded = false }: Props) {
  return (
    <span
      aria-hidden="true"
      style={{
        flex: "none",
        width: size,
        height: size,
        borderRadius: "50%",
        background: color,
        color: "#fff",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        fontFamily: "var(--font-display)",
        fontSize: Math.round(size * 0.52),
        opacity: faded ? 0.55 : 1,
      }}
    >
      {initial}
    </span>
  );
}
