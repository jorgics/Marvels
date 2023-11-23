package com.compose.marvels.domain.models

import com.compose.marvels.data.network.responses.ComicList
import com.compose.marvels.data.network.responses.Image

data class CharacterModel(
    val characterID: Int? = null,
    val name: String? = null,
    val description: String? = null,
    val image: Image? = null,
    val comics: ComicList? = null
)
