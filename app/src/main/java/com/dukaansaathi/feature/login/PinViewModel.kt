package com.dukaansaathi.feature.login

import androidx.lifecycle.ViewModel
import com.dukaansaathi.core.designsystem.PIN_LENGTH
import com.dukaansaathi.domain.repository.DukaanRepository
import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.viewmodel.orbitContainer

class PinViewModel(private val phone: String, private val repository: DukaanRepository) : ViewModel(), OrbitContainerHost<PinState, PinState, PinSideEffect> {
    override val container = orbitContainer<PinState, PinSideEffect>(PinState(phone, repository.isKnownPhone(phone)))

    fun onKey(key: String) = intent {
        val pin = if (key == "del") state.pin.dropLast(1) else if (state.pin.length < PIN_LENGTH) state.pin + key else state.pin
        reduce { state.copy(pin = pin, error = "") }
    }

    fun submit() = intent {
        if (state.returning && !repository.checkPin(phone, state.pin)) {
            reduce { state.copy(pin = "", error = "Wrong PIN. Try again.") }
        } else {
            repository.signIn(phone)
            postSideEffect(PinSideEffect.SignedIn)
        }
    }
}
