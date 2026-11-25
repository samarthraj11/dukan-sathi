package com.dukaansaathi.feature.adddistributor

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.viewmodel.orbitContainer

class AddDistributorViewModel : ViewModel(), OrbitContainerHost<AddDistributorState, AddDistributorState, Nothing> {
    override val container = orbitContainer<AddDistributorState, Nothing>(AddDistributorState())

    fun setNumber(number: String) = intent { reduce { state.copy(number = number, sent = false) } }
    fun send() = intent { if (state.number.length == 10) reduce { state.copy(sent = true) } }
}
