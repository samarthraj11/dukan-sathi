package com.dukaansaathi.feature.orders.presentation

import androidx.lifecycle.ViewModel
import com.dukaansaathi.domain.repository.DukaanRepository
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.viewmodel.orbitContainer

class OrdersViewModel(private val repository: DukaanRepository) : ViewModel(), OrbitContainerHost<OrdersState, OrdersState, Nothing> {
    override val container = orbitContainer<OrdersState, Nothing>(OrdersState(repository.distributors(), repository.orders()))

    fun refresh() = intent { reduce { state.copy(distributors = repository.distributors(), orders = repository.orders()) } }
    fun setFilter(filter: String) = intent { reduce { state.copy(filter = filter) } }
}
