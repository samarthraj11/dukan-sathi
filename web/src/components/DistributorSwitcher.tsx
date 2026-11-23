import { useState } from "react";
import { distributors } from "../data";
import { useApp } from "../state";
import DistributorChip from "./DistributorChip";
import DistributorSheet from "./DistributorSheet";

/** The chip showing the current distributor, plus the sheet it opens. */
export default function DistributorSwitcher({ compact = false }: { compact?: boolean }) {
  const { distributorId } = useApp();
  const [open, setOpen] = useState(false);
  const distributor = distributors.find((d) => d.id === distributorId)!;

  return (
    <>
      <DistributorChip distributor={distributor} compact={compact} onClick={() => setOpen(true)} />
      {open && <DistributorSheet onClose={() => setOpen(false)} />}
    </>
  );
}
