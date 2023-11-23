package com.compose.marvels.data.network.responses

import com.compose.marvels.domain.models.ComicModel
import com.google.gson.annotations.SerializedName
import java.util.Date

data class Comic(
    @SerializedName("title")
    val title: String?,
    @SerializedName("thumbnail")
    val image: Image?,
    @SerializedName("modified")
    val date: Date?
) {
    fun toDomain(): ComicModel = ComicModel(title, image, date)
}
