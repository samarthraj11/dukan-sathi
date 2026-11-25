package com.dukaansaathi.feature.catalogue.presentation

import com.dukaansaathi.core.model.*

enum class CatalogueFilter { All, Usual, Offers }

data class CatalogueState(
    val distributor: Distributor,
    val distributors: List<Distributor>,
    val query: String = "",
    val filter: CatalogueFilter = CatalogueFilter.All,
    val items: List<Product> = emptyList(),
    val quantities: Map<String, Qty> = emptyMap(),
    val totals: Totals,
)
