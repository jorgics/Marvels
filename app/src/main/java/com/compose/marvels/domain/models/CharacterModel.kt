package com.compose.marvels.domain.models

import com.compose.marvels.data.network.responses.ComicList
import com.compose.marvels.data.network.responses.Image

data class CharacterModel(
    val characterID: Int?,
    val name: String?,
    val description: String?,
    val image: Image?,
    val comics: ComicList?
)
