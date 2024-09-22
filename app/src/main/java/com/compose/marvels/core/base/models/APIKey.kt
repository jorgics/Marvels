package com.compose.marvels.core.base.models

data class APIKey(
    val apiKey: String = "",
    val privateKey: String = "",
    val isApiKey: Boolean = false,
    val enabled: Boolean = false
)
