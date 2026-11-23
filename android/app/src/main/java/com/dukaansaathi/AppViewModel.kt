package com.dukaansaathi

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.math.roundToInt

data class Line(val product: Product, val qty: Int, val total: Int)

data class Totals(val lines: List<Line>, val subtotal: Int, val gst: Int, val total: Int, val cartons: Int)

fun priceItems(items: Map<String, Int>): Totals {
    val lines = items.filterValues { it > 0 }.mapNotNull { (id, qty) ->
        products.firstOrNull { it.id == id }?.let { Line(it, qty, it.price * qty) }
    }.sortedBy { products.indexOf(it.product) } // catalogue order, not insertion order
    val subtotal = lines.sumOf { it.total }
    val gst = (subtotal * GST_RATE).roundToInt()
    return Totals(lines, subtotal, gst, subtotal + gst, lines.sumOf { it.qty })
}

/** All app state lives here, in memory. A real backend replaces this later. */
class AppViewModel : ViewModel() {
    var phone by mutableStateOf("")
    var signedIn by mutableStateOf(false)
    var distributorId by mutableStateOf("vaishnavi")

    // Cart is kept per distributor: key is "distributorId/productId", value is cartons.
    private val cart = mutableStateMapOf("vaishnavi/marie" to 5, "vaishnavi/glucose" to 3, "vaishnavi/cream" to 2)
    val orders = mutableStateListOf<Order>().apply { addAll(pastOrders) }

    val distributor: Distributor get() = distributors.first { it.id == distributorId }

    fun qty(productId: String): Int = cart["$distributorId/$productId"] ?: 0

    fun setQty(productId: String, qty: Int) {
        val key = "$distributorId/$productId"
        if (qty <= 0) cart.remove(key) else cart[key] = qty
    }

    private fun cartItems(): Map<String, Int> =
        cart.filterKeys { it.startsWith("$distributorId/") }.mapKeys { it.key.substringAfter('/') }

    fun totals(): Totals = priceItems(cartItems())

    fun fillCart(items: Map<String, Int>) = items.forEach { (id, q) -> setQty(id, q) }

    fun replaceCart(items: Map<String, Int>) {
        cart.keys.filter { it.startsWith("$distributorId/") }.forEach { cart.remove(it) }
        fillCart(items)
    }

    fun placeOrder(): Order? {
        val t = totals()
        if (t.cartons == 0) return null
        val order = Order(
            id = "o${System.currentTimeMillis()}",
            distributorId = distributorId,
            number = "#${1043 + orders.size - pastOrders.size}",
            date = "Today",
            amount = t.total,
            status = OrderStatus.Placed,
        )
        orders.add(0, order)
        cart.keys.filter { it.startsWith("$distributorId/") }.forEach { cart.remove(it) }
        return order
    }

    fun signOut() {
        signedIn = false
    }
}
