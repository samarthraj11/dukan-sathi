package com.dukaansaathi.feature.cart.presentation

sealed interface CartSideEffect {
    data class OrderPlaced(val orderId: String) : CartSideEffect
}
