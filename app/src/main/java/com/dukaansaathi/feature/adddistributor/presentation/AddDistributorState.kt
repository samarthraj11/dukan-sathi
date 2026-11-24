package com.dukaansaathi.feature.adddistributor.presentation

data class AddDistributorState(val number: String = "", val sent: Boolean = false) {
    val canSend: Boolean get() = number.length == 10 && !sent
}
