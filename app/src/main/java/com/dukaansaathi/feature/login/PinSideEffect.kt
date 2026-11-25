package com.dukaansaathi.feature.login

sealed interface PinSideEffect {
    data object SignedIn : PinSideEffect
}
