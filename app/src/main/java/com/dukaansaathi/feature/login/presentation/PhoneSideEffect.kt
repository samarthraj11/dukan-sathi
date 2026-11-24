package com.dukaansaathi.feature.login.presentation

sealed interface PhoneSideEffect {
    data class OpenPin(val phone: String) : PhoneSideEffect
}
