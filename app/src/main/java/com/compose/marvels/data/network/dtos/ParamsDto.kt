package com.compose.marvels.data.network.dtos

data class ParamsDto(
    val limit: Int = 20,
    val offset: Int = 0,
    val nameStartsWith: String = ""
)
