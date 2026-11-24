package com.dukaansaathi.feature.login.presentation

data class PhoneState(val phone: String = "") {
    val ready: Boolean get() = phone.length == 10
}
