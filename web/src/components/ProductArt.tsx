import type { Product } from "../data";

/** Simple drawn pack, tinted per product. Replaced by real photos later. */
export default function ProductArt({ art, size }: { art: Product["art"]; size?: number }) {
  return (
    <svg
      viewBox="0 0 64 64"
      width={size}
      height={size}
      aria-hidden="true"
      style={{ flex: "none", display: "block", width: size ?? "100%", height: size ?? "auto" }}
    >
      <rect width="64" height="64" rx="10" fill={art.bg} />
      <rect x="10" y="18" width="44" height="28" rx="3" fill="#fff" />
      <rect x="14" y="24" width="36" height="4" fill={art.stripe} />
      <rect x="14" y="32" width="24" height="3" fill={art.line} />
    </svg>
  );
}
