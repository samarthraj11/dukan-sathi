package com.dukaansaathi.feature.login.presentation

data class PinState(
    val phone: String,
    val returning: Boolean,
    val pin: String = "",
    val error: String = "",
)
