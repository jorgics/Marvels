package com.compose.marvels.domain.models

data class DetailModel(
    val characterModel: CharacterModel? = null,
    val comics: List<ComicModel>? = null,
    val error: Result? = null
)
