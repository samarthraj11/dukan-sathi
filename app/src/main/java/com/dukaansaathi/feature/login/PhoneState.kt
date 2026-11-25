package com.dukaansaathi.feature.login

data class PhoneState(val phone: String = "") {
    val ready: Boolean get() = phone.length == 10
}
