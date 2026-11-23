import { createContext, useContext, useMemo, useState, type ReactNode } from "react";
import { GST_RATE, pastOrders, products, usualOrder, type Order } from "./data";

type Cart = Record<string, Record<string, number>>; // distributorId -> productId -> cartons

type AppState = {
  phone: string;
  setPhone: (p: string) => void;
  signedIn: boolean;
  signIn: () => void;
  distributorId: string;
  selectDistributor: (id: string) => void;
  cart: Cart;
  setQty: (productId: string, qty: number) => void;
  fillCart: (items: Record<string, number>) => void;
  orders: Order[];
  placeOrder: () => Order | null;
};

const Ctx = createContext<AppState>(null!);
export const useApp = () => useContext(Ctx);

export function cartTotals(distributorId: string, cart: Cart) {
  const lines = Object.entries(cart[distributorId] ?? {})
    .filter(([, qty]) => qty > 0)
    .map(([productId, qty]) => {
      const product = products.find((p) => p.id === productId)!;
      return { product, qty, total: product.price * qty };
    });
  const subtotal = lines.reduce((sum, l) => sum + l.total, 0);
  const gst = Math.round(subtotal * GST_RATE);
  const cartons = lines.reduce((sum, l) => sum + l.qty, 0);
  return { lines, subtotal, gst, total: subtotal + gst, cartons };
}

export function AppStateProvider({ children }: { children: ReactNode }) {
  const [phone, setPhone] = useState("");
  const [signedIn, setSignedIn] = useState(false);
  const [distributorId, setDistributorId] = useState("sharma");
  const [cart, setCart] = useState<Cart>({ sharma: { marie: 5, glucose: 3, cream: 2 } });
  const [orders, setOrders] = useState<Order[]>(pastOrders);

  const value = useMemo<AppState>(
    () => ({
      phone,
      setPhone,
      signedIn,
      signIn: () => setSignedIn(true),
      distributorId,
      selectDistributor: setDistributorId,
      cart,
      setQty: (productId, qty) =>
        setCart((c) => ({
          ...c,
          [distributorId]: { ...c[distributorId], [productId]: Math.max(0, qty) },
        })),
      fillCart: (items) =>
        setCart((c) => ({ ...c, [distributorId]: { ...c[distributorId], ...items } })),
      orders,
      placeOrder: () => {
        const { total, cartons } = cartTotals(distributorId, cart);
        if (cartons === 0) return null;
        const order: Order = {
          id: `o${Date.now()}`,
          distributorId,
          number: `#${1043 + orders.length - pastOrders.length}`,
          date: "Today",
          amount: total,
          status: "Placed",
        };
        setOrders((o) => [order, ...o]);
        setCart((c) => ({ ...c, [distributorId]: {} }));
        return order;
      },
    }),
    [phone, signedIn, distributorId, cart, orders],
  );

  return <Ctx.Provider value={value}>{children}</Ctx.Provider>;
}

export { usualOrder };
