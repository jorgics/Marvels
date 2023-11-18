package com.compose.marvels.data.network.responses

import com.google.gson.annotations.SerializedName

data class ComicSummary(
    @SerializedName("resourceURI")
    val resourceURI: String?,
    @SerializedName("name")
    val name: String?
)
