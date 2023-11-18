package com.compose.marvels.data.network.responses

import com.google.gson.annotations.SerializedName

data class ComicDataContainer(
    @SerializedName("offset")
    val offset: Int?,
    @SerializedName("limit")
    val limit: Int?,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("count")
    val count: Int?,
    @SerializedName("results")
    val comics: List<Comic>?
)
