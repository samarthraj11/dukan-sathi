package com.dukaansaathi.feature.adddistributor

data class AddDistributorState(val number: String = "", val sent: Boolean = false) {
    val canSend: Boolean get() = number.length == 10 && !sent
}
