package com.dukaansaathi.feature.account

sealed interface AccountSideEffect {
    data object LoggedOut : AccountSideEffect
}
