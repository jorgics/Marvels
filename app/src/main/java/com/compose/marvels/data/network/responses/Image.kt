package com.compose.marvels.data.network.responses

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("path")
    val path: String?,
    @SerializedName("extension")
    val extension: String?
)
