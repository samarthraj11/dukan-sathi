package com.dukaansaathi.feature.cart

sealed interface CartSideEffect {
    data class OrderPlaced(val orderId: String) : CartSideEffect
}
