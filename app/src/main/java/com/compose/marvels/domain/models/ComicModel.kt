package com.compose.marvels.domain.models

import com.compose.marvels.data.network.responses.Image
import java.util.Date

data class ComicModel(
    val title: String?,
    val image: Image?,
    val date: Date?
)
