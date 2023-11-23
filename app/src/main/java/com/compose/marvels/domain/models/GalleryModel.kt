package com.compose.marvels.domain.models

data class GalleryModel(
    val total: Int? = null,
    val characters: List<CharacterModel>? = null,
    val error: Result? = null
)
