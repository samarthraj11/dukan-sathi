package com.dukaansaathi.feature.placed.presentation

import androidx.lifecycle.ViewModel
import com.dukaansaathi.domain.repository.DukaanRepository
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.viewmodel.orbitContainer

class OrderPlacedViewModel(orderId: String, repository: DukaanRepository) : ViewModel(), OrbitContainerHost<OrderPlacedState, OrderPlacedState, Nothing> {
    override val container = orbitContainer<OrderPlacedState, Nothing>(
        repository.order(orderId).let { order ->
            OrderPlacedState(order, order?.let { o -> repository.distributors().first { it.id == o.distributorId } })
        },
    )
}
