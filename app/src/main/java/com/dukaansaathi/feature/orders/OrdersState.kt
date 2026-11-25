package com.dukaansaathi.feature.orders

import com.dukaansaathi.core.model.*

const val ALL_DISTRIBUTORS = "all"

data class OrdersState(
    val distributors: List<Distributor>,
    val orders: List<Order>,
    val filter: String = ALL_DISTRIBUTORS,
) {
    val active: List<Distributor> get() = distributors.filter { it.status == DistributorStatus.Active }
    val monthTotal: Int get() = orders.sumOf { it.amount }
    val perDistributor: List<Pair<Distributor, Int>>
        get() = active.map { d -> d to orders.filter { it.distributorId == d.id }.sumOf { it.amount } }.filter { it.second > 0 }
    val shown: List<Order> get() = orders.filter { filter == ALL_DISTRIBUTORS || it.distributorId == filter }
}
