package com.dukaansaathi.feature.home.presentation

import androidx.lifecycle.ViewModel
import com.dukaansaathi.core.model.OrderStatus
import com.dukaansaathi.domain.pricing.priceItems
import com.dukaansaathi.domain.repository.DukaanRepository
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.viewmodel.orbitContainer

class HomeViewModel(private val repository: DukaanRepository) : ViewModel(), OrbitContainerHost<HomeState, HomeState, HomeSideEffect> {
    override val container = orbitContainer<HomeState, HomeSideEffect>(buildState())

    private fun buildState(): HomeState {
        val distributor = repository.selectedDistributor()
        val catalogue = repository.products()
        return HomeState(
            distributor = distributor,
            distributors = repository.distributors(),
            activeOrder = repository.orders().firstOrNull { it.distributorId == distributor.id && it.status != OrderStatus.Delivered },
            lastOrder = priceItems(repository.lastOrder(), catalogue),
            usual = repository.usualOrder().mapNotNull { (id, qty) -> catalogue.firstOrNull { it.id == id }?.let { it to qty } },
        )
    }

    fun refresh() = intent { reduce { buildState() } }

    fun selectDistributor(id: String) = intent {
        repository.selectDistributor(id)
        reduce { buildState() }
    }

    fun orderAgain() = intent {
        repository.replaceCart(repository.lastOrder())
        postSideEffect(HomeSideEffect.OpenCart)
    }

    fun addAllUsual() = intent {
        repository.fillCart(repository.usualOrder())
        postSideEffect(HomeSideEffect.OpenCart)
    }
}
