package com.dukaansaathi.feature.home.presentation

sealed interface HomeSideEffect {
    data object OpenCart : HomeSideEffect
}
