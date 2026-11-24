package com.dukaansaathi.feature.login.presentation

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.viewmodel.orbitContainer

class PhoneViewModel : ViewModel(), OrbitContainerHost<PhoneState, PhoneState, PhoneSideEffect> {
    override val container = orbitContainer<PhoneState, PhoneSideEffect>(PhoneState())

    fun setPhone(phone: String) = intent { reduce { state.copy(phone = phone) } }
    fun continueToPin() = intent { if (state.ready) postSideEffect(PhoneSideEffect.OpenPin(state.phone)) }
}
