# Dukaan Saathi: MVP Spec

Order and delivery app for a biscuit distributor and the shops (kirana and wholesale) it supplies.
Goal: replace phone calls, WhatsApp messages and handwritten slips. Target users are tier 2-3 shopkeepers, so the UI is simple, large-button and Hindi-first.

## Assumptions (change if wrong)
- One distributor, roughly 50-300 shops, one city.
- The distributor has its own delivery staff.
- Shops buy on a mix of credit and pay-on-delivery. Each shop has a credit limit.
- Android phones, weak networks. Build a PWA first (no Play Store wait).

## Roles
| Role | Can do |
|---|---|
| Shop | Browse catalogue, order, reorder, track, view invoices and ledger |
| Owner | Everything, plus pricing, credit limits, approvals, reports |
| Order desk | Inbox: accept, modify, reject orders; place orders for shops |
| Warehouse | Pick list, packing, stock |
| Delivery | Stop list, mark delivered, record collection |

## MVP features (Phase 1)
Shop app
1. Phone OTP login; owner approves new shops.
2. Catalogue: photo, pack size (carton / dozen / piece), shop-specific price, GST, stock badge. Search plus a "My frequent items" tab.
3. Cart with +/- carton steppers and a live total (price + GST) before confirm.
4. One-tap reorder of last or any past order.
5. Order status: Placed, Confirmed, Packed, Out for delivery, Delivered. Edit or cancel until Packed.
6. Invoice view (PDF) and basic ledger: outstanding, payments, credit limit.
7. Call / WhatsApp the salesperson button.
8. Offline cart: build an order offline, sync on reconnect.

Distributor side (web)
1. Order inbox with auto-validation flags: credit limit exceeded, overdue dues, out of stock.
2. Accept / modify / reject, with the shop notified automatically.
3. Product, price tier and shop management; stock quantity per SKU.
4. Aggregated pick list per delivery day, per-shop packing slip.
5. GST invoice generation.
6. Record payments (cash / UPI) against a shop's ledger.
7. Delivery view: mark out-for-delivery and delivered.

Notifications: WhatsApp (Cloud API) for confirmed, out for delivery and delivered, using approved templates. Push is secondary.

## Later phases
- Phase 2: schemes ("buy 10 get 1"), batch and expiry with FIFO, delivery app with proof of delivery (OTP/photo), UPI collection, returns and credit notes, WhatsApp ordering.
- Phase 3: route planning, demand forecast, salesman offline app, analytics, Tally export.

## Data model (Postgres)
- `shops` (id, name, owner_name, phone, gstin, address, pincode, lat, lng, zone_id, price_tier_id, credit_limit, status[pending|active|blocked])
- `users` (id, phone, name, role, shop_id nullable)
- `zones` (id, name, delivery_days[])
- `products` (id, name, brand, image_url, hsn, gst_pct, active)
- `skus` (id, product_id, pack_type[carton|dozen|piece], units_per_pack, mrp, stock_qty)
- `price_tiers` (id, name); `sku_prices` (sku_id, price_tier_id, price); `shop_sku_price` (shop_id, sku_id, price) for negotiated overrides
- `orders` (id, shop_id, placed_by_user_id, source[app|salesman|whatsapp], status, subtotal, gst_total, total, flags jsonb, delivery_date, created_at)
- `order_items` (id, order_id, sku_id, qty, unit_price, gst_pct, line_total)
- `order_events` (id, order_id, actor_id, from_status, to_status, note, at): the audit trail
- `invoices` (id, order_id, number, pdf_url, issued_at)
- `ledger_entries` (id, shop_id, type[invoice|payment|credit_note|adjustment], ref_id, amount, mode[cash|upi|bank], created_by, at); the shop balance is the sum of entries
- `deliveries` (id, order_id, driver_id, status, delivered_at, collected_amount, failure_reason)

Rules: stock decrements on order confirmation. Credit check is `balance + order total <= credit_limit`. Edits are allowed only before status `packed`. Every status change writes an `order_events` row.

## Suggested stack
- Frontend: React PWA (Vite), i18n (Hindi / English), service worker plus IndexedDB for the offline cart.
- Backend: Supabase (Postgres, auth with phone OTP, storage) or Node + Postgres.
- Messaging: WhatsApp Cloud API; OTP via SMS provider.
- Invoices: server-side PDF (e.g. headless HTML-to-PDF).

## Multi-distributor model (shop picks who to order from)
The platform is not tied to one distributor. A shop has one account and links to many distributors.
- New tables: `distributors` (id, name, phone, brand colour, status) and `distributor_shops` (shop_id, distributor_id, status[pending|active|blocked], credit_limit, price_tier_id, approved_at).
- `products`, `skus`, `price_tiers`, `orders`, `ledger_entries` and staff `users` all carry a `distributor_id`. A shop sees only the data of the distributor it has selected.
- Linking: the shop adds a distributor by phone number or QR/invite link, the distributor approves (and sets credit terms), then the shop can order.
- Distributors are onboarded by the platform team (their number is pre-registered); any other number that signs up is a shop.
- Shop UI: a distributor switcher at the top of Home and Catalogue; Orders lists orders across distributors with a distributor tag.

## Open questions
1. How many shops, and how many orders a day?
2. Own delivery staff or third party?
3. Credit vs pay-on-delivery split? (drives ledger priority)
4. Main language and city? (finalises the name and UI copy)
5. Do you already issue GST invoices through Tally or other software?

## Build order
1. Schema plus seed data (shops, SKUs, tiers)
2. Shop login, catalogue, cart, order placement
3. Distributor inbox with validation, accept/reject
4. Stock, pick list, invoice PDF
5. Delivery status plus ledger and payments
6. WhatsApp notifications, offline cart
