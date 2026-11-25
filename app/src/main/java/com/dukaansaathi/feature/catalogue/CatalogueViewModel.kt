package com.dukaansaathi.feature.catalogue

import androidx.lifecycle.ViewModel
import com.dukaansaathi.core.model.Qty
import com.dukaansaathi.domain.repository.DukaanRepository
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.viewmodel.orbitContainer

class CatalogueViewModel(private val repository: DukaanRepository) : ViewModel(), OrbitContainerHost<CatalogueState, CatalogueState, Nothing> {
    override val container = orbitContainer<CatalogueState, Nothing>(buildState(CatalogueState(repository.selectedDistributor(), repository.distributors(), totals = repository.totals())))

    private fun buildState(base: CatalogueState): CatalogueState {
        val usualIds = repository.usualOrder().keys
        val all = repository.products()
        val items = all
            .filter {
                when (base.filter) {
                    CatalogueFilter.All -> true
                    CatalogueFilter.Usual -> it.id in usualIds
                    CatalogueFilter.Offers -> it.offer != null
                }
            }
            .filter { it.name.contains(base.query.trim(), ignoreCase = true) }
        return base.copy(
            distributor = repository.selectedDistributor(),
            distributors = repository.distributors(),
            items = items,
            quantities = all.associate { it.id to repository.qty(it.id) },
            totals = repository.totals(),
        )
    }

    fun refresh() = intent { reduce { buildState(state) } }
    fun setQuery(query: String) = intent { reduce { buildState(state.copy(query = query)) } }
    fun setFilter(filter: CatalogueFilter) = intent { reduce { buildState(state.copy(filter = filter)) } }

    fun setQty(productId: String, qty: Qty) = intent {
        repository.setQty(productId, qty)
        reduce { buildState(state) }
    }

    fun selectDistributor(id: String) = intent {
        repository.selectDistributor(id)
        reduce { buildState(state) }
    }
}
