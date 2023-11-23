package com.compose.marvels.data.network.responses

import com.google.gson.annotations.SerializedName

data class CharacterDataWrapper(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("copyright")
    val copyright: String?,
    @SerializedName("attributionText")
    val attributionText: String?,
    @SerializedName("attributionHTML")
    val attributionHTML: String?,
    @SerializedName("data")
    val data: CharacterDataContainer?,
    @SerializedName("etag")
    val etag: String?
)
