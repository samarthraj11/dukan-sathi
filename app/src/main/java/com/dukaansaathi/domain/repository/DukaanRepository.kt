package com.dukaansaathi.domain.repository

import com.dukaansaathi.core.model.Distributor
import com.dukaansaathi.core.model.Order
import com.dukaansaathi.core.model.Product
import com.dukaansaathi.core.model.Totals

/** Everything the screens need. The in-memory version is replaced by a backend later. */
interface DukaanRepository {
    // Session
    fun isKnownPhone(phone: String): Boolean
    fun checkPin(phone: String, pin: String): Boolean
    fun signIn(phone: String)
    fun signOut()
    fun phone(): String

    // Distributors
    fun distributors(): List<Distributor>
    fun selectedDistributor(): Distributor
    fun selectDistributor(id: String)

    // Catalogue and cart, always for the selected distributor
    fun products(): List<Product>
    fun usualOrder(): Map<String, Int>
    fun lastOrder(): Map<String, Int>
    fun qty(productId: String): Int
    fun setQty(productId: String, qty: Int)
    fun fillCart(items: Map<String, Int>)
    fun replaceCart(items: Map<String, Int>)
    fun totals(): Totals

    // Orders from every distributor
    fun orders(): List<Order>
    fun order(id: String): Order?
    fun placeOrder(): Order?
}
