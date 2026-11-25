package com.dukaansaathi.data.repository

import com.dukaansaathi.core.model.Distributor
import com.dukaansaathi.core.model.Order
import com.dukaansaathi.core.model.OrderStatus
import com.dukaansaathi.core.model.Product
import com.dukaansaathi.core.model.Qty
import com.dukaansaathi.core.model.Totals
import com.dukaansaathi.data.local.DEMO_PIN
import com.dukaansaathi.data.local.dummyDistributors
import com.dukaansaathi.data.local.dummyLastOrder
import com.dukaansaathi.data.local.dummyPastOrders
import com.dukaansaathi.data.local.dummyProducts
import com.dukaansaathi.data.local.dummyUsualOrder
import com.dukaansaathi.data.local.knownPhones
import com.dukaansaathi.domain.pricing.priceItems
import com.dukaansaathi.domain.repository.DukaanRepository

/** All app data lives in memory and resets when the app process dies. */
class InMemoryDukaanRepository : DukaanRepository {
    private var phone = ""
    private var distributorId = "vaishnavi"

    // Cart is kept per distributor: key is "distributorId/productId", value is peti and bundles.
    private val cart = mutableMapOf("vaishnavi/marie" to Qty(peti = 1), "vaishnavi/glucose" to Qty(bundle = 3), "vaishnavi/cream" to Qty(bundle = 2))
    private val orders = dummyPastOrders.toMutableList()

    override fun isKnownPhone(phone: String) = phone in knownPhones
    override fun checkPin(phone: String, pin: String) = pin == DEMO_PIN
    override fun signIn(phone: String) { this.phone = phone }
    override fun signOut() { phone = "" }
    override fun phone() = phone

    override fun distributors(): List<Distributor> = dummyDistributors
    override fun selectedDistributor(): Distributor = dummyDistributors.first { it.id == distributorId }
    override fun selectDistributor(id: String) { distributorId = id }

    override fun products(): List<Product> = dummyProducts.filter { it.distributorId == distributorId }
    override fun usualOrder(): Map<String, Qty> = dummyUsualOrder[distributorId].orEmpty()
    override fun lastOrder(): Map<String, Qty> = dummyLastOrder[distributorId].orEmpty()

    override fun qty(productId: String) = cart["$distributorId/$productId"] ?: Qty()

    override fun setQty(productId: String, qty: Qty) {
        val key = "$distributorId/$productId"
        if (qty.isEmpty) cart.remove(key) else cart[key] = qty
    }

    private fun clearCart() = cart.keys.filter { it.startsWith("$distributorId/") }.forEach { cart.remove(it) }

    override fun fillCart(items: Map<String, Qty>) = items.forEach { (id, q) -> setQty(id, q) }

    override fun replaceCart(items: Map<String, Qty>) {
        clearCart()
        fillCart(items)
    }

    override fun totals(): Totals {
        val items = cart.filterKeys { it.startsWith("$distributorId/") }.mapKeys { it.key.substringAfter('/') }
        return priceItems(items, dummyProducts)
    }

    override fun orders(): List<Order> = orders.toList()
    override fun order(id: String) = orders.firstOrNull { it.id == id }

    override fun placeOrder(): Order? {
        val t = totals()
        if (t.pcs == 0) return null
        val order = Order(
            id = "o${System.currentTimeMillis()}",
            distributorId = distributorId,
            number = "#${1043 + orders.size - dummyPastOrders.size}",
            date = "Today",
            amount = t.total,
            status = OrderStatus.Placed,
        )
        orders.add(0, order)
        clearCart()
        return order
    }
}
