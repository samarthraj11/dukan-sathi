package com.dukaansaathi.feature.login

sealed interface PhoneSideEffect {
    data class OpenPin(val phone: String) : PhoneSideEffect
}
