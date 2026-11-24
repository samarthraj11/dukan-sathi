package com.dukaansaathi.feature.cart.presentation

import com.dukaansaathi.core.model.*

data class CartState(
    val distributor: Distributor,
    val totals: Totals,
    val hasOffer: Boolean,
) {
    val toGo: Int get() = OFFER_THRESHOLD - totals.subtotal
}
