package com.dukaansaathi.feature.cart

import androidx.lifecycle.ViewModel
import com.dukaansaathi.core.model.Qty
import com.dukaansaathi.domain.repository.DukaanRepository
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.viewmodel.orbitContainer

class CartViewModel(private val repository: DukaanRepository) : ViewModel(), OrbitContainerHost<CartState, CartState, CartSideEffect> {
    override val container = orbitContainer<CartState, CartSideEffect>(buildState())

    private fun buildState() = CartState(
        distributor = repository.selectedDistributor(),
        totals = repository.totals(),
        hasOffer = repository.products().any { it.offer != null },
    )

    fun refresh() = intent { reduce { buildState() } }

    fun setQty(productId: String, qty: Qty) = intent {
        repository.setQty(productId, qty)
        reduce { buildState() }
    }

    fun placeOrder() = intent {
        repository.placeOrder()?.let { postSideEffect(CartSideEffect.OrderPlaced(it.id)) }
    }
}
