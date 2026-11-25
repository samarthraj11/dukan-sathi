package com.dukaansaathi.feature.home

sealed interface HomeSideEffect {
    data object OpenCart : HomeSideEffect
}
