package com.dukaansaathi.feature.account.presentation

sealed interface AccountSideEffect {
    data object LoggedOut : AccountSideEffect
}
