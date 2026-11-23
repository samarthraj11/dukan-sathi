// Dummy data for the UI-only build. Replaced by the backend later.

export type Distributor = {
  id: string;
  name: string;
  initial: string;
  color: string;
  status: "active" | "pending";
  note: string;
  tone: "ok" | "plain" | "wait";
};

export const distributors: Distributor[] = [
  { id: "sharma", name: "Sharma Biscuits", initial: "S", color: "var(--c-orange)", status: "active", note: "1 order on the way", tone: "ok" },
  { id: "verma", name: "Verma Traders", initial: "V", color: "var(--c-teal)", status: "active", note: "Due ₹2,400", tone: "plain" },
  { id: "jain", name: "Jain Foods", initial: "J", color: "var(--c-purple)", status: "pending", note: "Waiting for approval", tone: "wait" },
];

export type Product = {
  id: string;
  distributorId: string;
  name: string;
  price: number; // per carton, before GST
  stock: "in" | "low" | "out";
  left?: number;
  offer?: string;
  art: { bg: string; stripe: string; line: string };
};

export const products: Product[] = [
  { id: "marie", distributorId: "sharma", name: "Marie Gold", price: 456, stock: "in", offer: "10+1 free", art: { bg: "#E7C46A", stripe: "#C4533F", line: "#1C1C1C" } },
  { id: "glucose", distributorId: "sharma", name: "Glucose Plus", price: 540, stock: "in", art: { bg: "#6FBF73", stripe: "#3E9B66", line: "#1C1C1C" } },
  { id: "rusk", distributorId: "sharma", name: "Milk Rusk", price: 360, stock: "low", left: 4, art: { bg: "#EE8B6E", stripe: "#1C1C1C", line: "#C4533F" } },
  { id: "cream", distributorId: "sharma", name: "Cream Sandwich", price: 720, stock: "in", art: { bg: "#9B8CF0", stripe: "#1C1C1C", line: "#5A5A5A" } },
  { id: "digestive", distributorId: "verma", name: "Digestive Wheat", price: 600, stock: "in", art: { bg: "#D8B27A", stripe: "#3A9A9C", line: "#1C1C1C" } },
  { id: "crackers", distributorId: "verma", name: "Salted Crackers", price: 420, stock: "in", art: { bg: "#8FC7C9", stripe: "#1C1C1C", line: "#3A9A9C" } },
  { id: "choco", distributorId: "verma", name: "Choco Cream", price: 690, stock: "low", left: 6, art: { bg: "#B98C7A", stripe: "#F8DE96", line: "#1C1C1C" } },
];

export type Order = {
  id: string;
  distributorId: string;
  number: string;
  date: string;
  amount: number;
  status: "Placed" | "On the way" | "Delivered";
};

export const pastOrders: Order[] = [
  { id: "o1042", distributorId: "sharma", number: "#1042", date: "Sat 22 Nov", amount: 5880, status: "On the way" },
  { id: "o218", distributorId: "verma", number: "#218", date: "Fri 21 Nov", amount: 2400, status: "Delivered" },
  { id: "o1038", distributorId: "sharma", number: "#1038", date: "Mon 17 Nov", amount: 6301, status: "Delivered" },
  { id: "o214", distributorId: "verma", number: "#214", date: "Tue 11 Nov", amount: 3150, status: "Delivered" },
];

// "Usual" order shown on Home: quantities in cartons.
export const usualOrder: Record<string, Record<string, number>> = {
  sharma: { marie: 5, glucose: 3, rusk: 2 },
  verma: { digestive: 4, crackers: 2 },
};

export const GST_RATE = 0.18;
export const OFFER_THRESHOLD = 6000;

export const formatRupees = (n: number) => `₹${n.toLocaleString("en-IN")}`;
