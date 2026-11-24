package com.dukaansaathi.feature.login.presentation

sealed interface PinSideEffect {
    data object SignedIn : PinSideEffect
}
