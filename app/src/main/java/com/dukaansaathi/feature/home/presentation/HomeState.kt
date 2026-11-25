package com.dukaansaathi.feature.home.presentation

import com.dukaansaathi.core.model.*

data class HomeState(
    val distributor: Distributor,
    val distributors: List<Distributor>,
    val activeOrder: Order?,
    val lastOrder: Totals,
    val usual: List<Pair<Product, Qty>>,
)
